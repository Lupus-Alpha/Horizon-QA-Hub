import datetime
import os
import unittest
from uuid import uuid1

from core.api.apitestcase import ApiTestCase
from core.web.webtestcase import UiTestCase
BASE_PATH = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
IMAGE_PATH = os.path.join(BASE_PATH, "image")

class Case(unittest.TestCase):
    def __init__(self, case_name, test_data, case_type="API"):
        self.test_data = test_data
        self.case_type = case_type
        self.trans_list = []
        self.case_name = case_name
        unittest.TestCase.__init__(self, case_name)

    def testEntrance(self):
        if self.case_type == "API":

            ApiTestCase(test=self).execute_case() # self 意思是传入当前的用例实例，从而可以在ApiTestCase中调用Case的方法及属性
        elif self.case_type == "WEB":
            UiTestCase(test=self).execute()
        else:
            pass

    def doCleanups(self):
        """重写doCleanups 自定义结果处理"""
        unittest.TestCase.doCleanups(self)  # 父类方法仍然需要执行
        self.handleResult()
    def recordTransDuring(self, during):
        """记录事务执行时长"""
        if len(self.trans_list) > 0:
            self.trans_list[-1]["during"] = during


    def defineTrans(self, id, name, content=""):
        """定义事务"""
        trans_dict = {
            "id": id,  # apiId/operationId
            "name": name,  # apiName/operationName
            "content": content,  # api path/operation element
            "log": "",  # 步骤的日志
            "during": 0,  # 执行时间
            "status": "",  # 执行结果 成功失败错误跳过
            "screenShotList": []  # 截图列表 放的是截图的uuid 截图最好异步上传
        }
        self.trans_list.append(trans_dict)
        if len(self.trans_list) > 1 and self.trans_list[-2]["status"] == "":
            self.trans_list[-2]["status"] = 0  # 如果上一个事务没有结果，记录上一个trans状态为通过

    def handleResult(self):
        """结果处理"""
        if len(self.trans_list) == 0:
            self.defineTrans(self.case_name.split("_")[1], "未知")
        isFail = False
        isError = False
        error_type = None
        error_value = None
        error_tb = None
        # 处理用例执行过程中的错误和失败 以此来判断用例最终状态
        """_outcome.errors中第一是setup的,第二乃至第n-1个是用例的, 最后一个才是teardown的"""
        for index, (test, exc_info) in enumerate(self._outcome.errors):
            if exc_info is not None:  # setup/teardown也会记录该信息 但是记录为空
                if issubclass(exc_info[0], AssertionError):  # 判断是否断言错误 是的话就是失败
                    isFail = True
                    if not isError:  # 默认错误优先级高
                        error_type = AssertionError
                        error_value = exc_info[1]
                        error_tb = exc_info[2]
                else:
                    isError = True
                    error_type = exc_info[0] # 异常类型
                    error_value = exc_info[1] # 异常实例
                    error_tb = exc_info[2] # 异常回溯信息
        # 根据用例原始成功状态来判断最后一个事务是否是成功的
        if self._outcome.success is True:  # 如果用例最终是成功的，但是记录过失败或错误 那最终还是失败或错误
            if self.trans_list[-1]["status"] == "":  # 如果最后一个事务没有状态，则设为pass
                self.trans_list[-1]["status"] = 0
            if isError or isFail:  # 有错误或者失败的话用例修改状态
                self._outcome.errors.clear()
                self._outcome.errors.append((self, (error_type, error_value, error_tb)))
                self._outcome.success = False # 一个用例中只要有一个事务是失败的整个用例就是失败的
        else:
            # 如果用例原始成功状态为否 则说明最后一个事务是失败或者错误的
            exc_info = self._outcome.errors[-2][-1]  # 倒数第二个errors是最后一个事务的
            if issubclass(exc_info[0], AssertionError):
                self.trans_list[-1]["status"] = 1  # 最后一步设为fail
            else:
                self.errorLog(str(exc_info[1]))  # 记录最后一步的错误信息
                self.trans_list[-1]["status"] = 2  # 最后一步设为error
            self._outcome.errors.clear()
            self._outcome.errors.append((self, (error_type, error_value, error_tb)))

    def errorLog(self, log_info):
        """记录错误信息"""
        if len(self.trans_list) > 0:
            current_time = datetime.datetime.now()
            log = "%s - Error - %s" % (current_time.strftime('%Y-%m-%d %H:%M:%S.%f'), log_info)
            if self.trans_list[-1]["log"] != "":
                if self.case_type == "API":
                    log = "<br><br>" + log
                else:
                    log = "<br>" + log
            self.trans_list[-1]["log"] = self.trans_list[-1]["log"]+ log


    def debugLog(self, log_info):
        """debug日志"""
        if len(self.trans_list) > 0:
            # 事务名，执行结果，日志
            current_time = datetime.datetime.now()
            log = "%s - Debug - %s" % (current_time.strftime('%Y-%m-%d %H:%M:%S.%f'), log_info)
            if self.trans_list[-1]["log"] != "":
                if self.case_type == "API":
                    log = "<br><br>" + log  # <br> 换行符
                else:
                    log = "<br>" + log
            self.trans_list[-1]["log"] = self.trans_list[-1]["log"] + log  # 将日志记录在执行的用例的最后一个action中

    def recordFailStatus(self, exc_info=None):
        """记录断言失败"""
        self._outcome.errors.append((self, exc_info))   # _outcome主要用来存放错误信息的
        if len(self.trans_list) > 0:
            self.trans_list[-1]["status"] = 1   # 记录当前事务为失败 失败一般指的是断言失败
            self.errorLog(str(exc_info[1]))

    def recordErrorStatus(self, exc_info=None):
        """记录程序错误"""
        self._outcome.errors.append((self, exc_info))
        if len(self.trans_list) > 0:
            self.trans_list[-1]["status"] = 2  # 记录当前事务为错误 出断言失败的错误均记录为错误
            self.errorLog(str(exc_info[1]))

    def saveScreeShot(self,screenShot):
        """保存截图"""
        uuid = str(uuid1())
        task_id = getattr(self, "task_id")
        task_image_path = os.path.join(IMAGE_PATH, task_id)
        if not os.path.exists(task_image_path):
            os.makedirs(task_image_path)
        file_name = "%s.png"%uuid
        screenShotPath = os.path.join(task_image_path,file_name)
        with open(screenShotPath,"wb") as f:
            f.write(screenShot)



