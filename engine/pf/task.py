import os
import threading
import unittest
import zipfile

from requests import Session

from config.config import DATA_PATH
from pf.api import Api
from pf.case import Case
from pf.config import Config
from pf.log import ErrorLogger, DebugLogger
from pf.result import Result


class Task(object):
    def __init__(self, task):
        self.task = task
        self.data_path = DATA_PATH
        self.config = Config()



    def get_task_file(self):
        download_url = self.task["downloadUrl"]
        if not os.path.exists(self.data_path):
              os.makedirs(self.data_path)
        try:
            file = Api().download_task_file(download_url)
        except Exception as e:
            ErrorLogger("数据拉取失败 错误信息: %s" % str(e))
            return None
        else:
            if file is not None:
                file_path = os.path.join(self.data_path, str(self.task["taskId"]) + ".zip")
                with open(file_path, 'wb+') as f:
                    for chunk in file.iter_content(chunk_size=1024):
                        if chunk:
                            f.write(chunk)
                f.close()
                DebugLogger("数据拉取成功")
                return file_path
            else:
                return None

    def unzip_task_file(self, file_path):
        is_zip_file= zipfile.is_zipfile(file_path)
        if is_zip_file:
            with zipfile.ZipFile(file_path, 'r') as fz:
                for file in fz.namelist():
                    fz.extract(file, self.data_path)
        os.remove(file_path)


    def task_analysis(self):
        test_paln = {}
        taske_Type = self.task["taskType"]
        if taske_Type != 'debug':
            file_path = self.get_task_file()
            if file_path is not None:
                self.unzip_task_file(file_path)
            for collection_map in self.task["testCollectionList"]:
                collection = collection_map["collectionId"]
                test_case_list = collection_map["testCaseList"]
                driver = {
                    "browser_opt": self.config.browser_opt,
                    "browser_path": self.config.browser_path,
                    "driver": None
                }
                session = Session()
                context = dict()
                for case in test_case_list:
                    test_case = {
                        "driver": driver,
                        "session": session,
                        "context": context,
                        "task_id": self.task["taskId"],
                        "test_type": case["caseType"],
                        "index": case["index"],
                        "test_class": "class_" + collection,
                        "test_case": "case_%s_%s" % (case["caseId"], case["index"]),
                        "test_data": os.path.join(self.data_path, self.task["taskId"], collection,
                                                  case["caseId"] + ".json")
                    }
                    if collection not in test_paln.keys():
                        test_paln[collection] = []
                    test_paln[collection].append(test_case)
        else:
            collection_map = self.task["testCollectionList"][0]
            collection = collection_map["collectionId"]
            driver = {
                "browser_opt": self.config.browser_opt,
                "browser_path": self.config.browser_path,
                "driver": None
            }
            session = Session()
            context = dict()
            test_case = {
                "driver": driver,
                "session": session,
                "context": context,  # 存放用例执行过程中的变量 即变量空间域
                "index": collection_map["testCaseList"][0]["index"],
                "task_id": self.task["taskId"],
                "test_type": collection_map["testCaseList"][0]["caseType"],
                "test_class": "class_" + collection,
                "test_case": "case_%s_%s" % (collection_map["testCaseList"][0]["caseId"], collection_map["testCaseList"][0]["index"]),
                "test_data": self.task["debugData"]
            }
            if collection not in test_paln.keys():
                test_paln[collection] = []
            test_paln[collection].append(test_case)
        return test_paln

    def create_task(self,plan,report_queue,current_exec_status):
        if self.task["reRun"] == False:
            run_times = 1
        else:
            run_times = 2
        report_queue.put("run_all_start--%s" % self.task["taskId"])
        for i in range(run_times):
            if i == 0:
                test_plan = plan
            else:
                 test_plan = plan
            default_result = []  # 记录所有用例的执行结果 从而能分析出哪些用例失败了
            if len(test_plan) > 0:
                default_lock = threading.RLock()
                for collection, test_case_list in test_plan.items():
                    threads = []
                    t = threading.Thread(target=self.run_test, args=(test_case_list, default_lock, default_result, i + 1, report_queue))
                    threads.append(t)
                    for t in threads:
                        t.start()
                    # for t in threads:
                        t.join()
        print("任务执行完成,开始报告上传")
        report_queue.put("run_all_stop--%s" % self.task["taskId"])  # 告诉报告进程 用例执行完成了
        current_exec_status.value = 1  # 告诉截图上传进程已经执行结束


    def run_test(self,test_case_list,default_lock,default_result,run_index,queue):
        """
        执行任务
        :param test_case_list: 从测试计划中遍历出来的测试用例列表
        :param default_lock:
        :param default_result:
        :param run_index:
        :param queue: 报告队列
        :return:
        """
        suite = unittest.TestSuite()
        for test_case in test_case_list:

            cls_name = test_case["test_class"]
            try:
                cls = eval(cls_name)  # 如果class已经创建 直接获取
            except:
                cls = type(cls_name, (Case,), {'__doc__': cls_name})  # 动态创建一个testcase类

            case_name = test_case["test_case"]
            case_type = test_case["test_type"]

            """"所有测试均通过该入口启动"""
            setattr(cls, case_name, Case.testEntrance)  # 定义每个测试用例需要执行的函数
            case_data = test_case["test_data"]
            test_cases = cls(case_name, case_data, case_type)
            test_cases.run_index = test_case["index"]
            test_cases.session = test_case["session"]
            test_cases.context = test_case["context"]
            test_cases.driver = test_case["driver"]
            suite.addTest(test_cases)
        result = Result(default_result, default_lock, queue, verbosity=2)
        try:
            print(f"执行套件初始化成功：{suite}")
            suite(result)   # 通过测试用例的回调方法来启动用例执行，隐式地使用了 TextTestRunner 来运行测试。
            # 执行测试用例
        except Exception as ex:
            ErrorLogger("Failed to run test(RunTime:run%s & ThreadName:%s), Error info:%s" %
                        (run_index, threading.current_thread().name, ex))









