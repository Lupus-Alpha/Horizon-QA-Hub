# -*- coding: utf-8 -*-
import datetime
import sys
import unittest


class Result(unittest.TestResult):
    '''
    用例执行结果
    0: 通过
    1: 失败
    2: 错误

    '''
    # note: _TestResult is a pure representation of results.
    def __init__(self, result, lock, queue, verbosity=1):
        unittest.TestResult.__init__(self)
        self.verbosity = verbosity
        # 记录多线程下所有用例执行结果， 从而能分析出哪些用例失败了
        self.default_result = result
        self.default_lock = lock
        self.queue = queue #报告队列
        # result 记录用例执行结果，每个元素是一个元组，元组中包含三个元素，分别是用例执行状态，用例实例，错误信息，后续
        #进一步处理之后，会将用例执行结果放入到default_result中
        self.result = []

    def startTest(self, test):
        unittest.TestResult.startTest(self, test)
        test.start_time = datetime.datetime.now()

    def stopTest(self, test):
        """结束测试 输出数据"""
        test.stop_time = datetime.datetime.now()
        # 动态更新执行结果 通知报告进程
        if self.default_lock.acquire():
            status, test_case, error = self.result[-1]  # 每次只处理最新的那个
            case_info = {
                "status": status,
                "startTime": test_case.start_time.timestamp()*1000,
                "endTime": test_case.stop_time.timestamp()*1000,
                "collectionId": test_case.__class__.__doc__.split("_")[-1], # 获取类名
                "caseId": getattr(test, "case_name", " _ ").split("_")[1],
                "caseType": getattr(test, "case_type", "API"),
                "caseName": getattr(test, "test_case_name", "未知"),
                "caseDesc": getattr(test, "test_case_desc", None),
                "index": int(getattr(test, "case_name", " _0").split("_")[-1]),
                "transactionList": test_case.trans_list
            }
            self.default_result.append(case_info)   # 加入到所有集合公用的变量空间 从而方便后面读取错误用例
            self.queue.put(case_info)
            self.default_lock.release()
            print("用例执行结果详细信息", case_info)

    def addSuccess(self, test):
        unittest.TestResult.addSuccess(self, test)
        self.mergeResult(0, test, "")

    def addFailure(self, test, err):
        unittest.TestResult.addFailure(self, test, err)
        _, _exc_str = self.failures[-1]
        self.mergeResult(1, test, _exc_str)

    def addError(self, test, err):
        unittest.TestResult.addError(self, test, err)
        _, _exc_str = self.errors[-1]
        self.mergeResult(2, test, _exc_str)

    def mergeResult(self, n, test, e):
        """将用例元素的步骤和状态整合在一起"""
        self.result.append((n, test, e))    # test: 每个用例的实例对象
