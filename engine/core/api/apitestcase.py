import sys

from core.api.collect import ApiCollect
from core.api.teststeps import ApiTestStep
from tools.utils import get_case_message, replace_variables


class ApiTestCase:
    """用例类，用来记录接口的相关信息内容，以及执行接口的步骤"""

    def __init__(self, test):
        self.test = test  # Case实例，包含了test_data和case_type
        self.case_message = get_case_message(test.test_data)    # 读取json模板信息
        self.session = test.session  # Session实例
        self.context = test.context  # 存储依赖参数
        # 整理用例基本信息
        self.id = self.case_message['caseId']  # 用例id
        self.name = self.case_message['caseName']  # 用例name
        self.params = self.case_message['params']
        setattr(test, 'test_case_name', self.case_message['caseName'])  # 记录用例名称
        setattr(test, 'test_case_desc', self.case_message['comment'])   # 记录用例的描述
        self.functions = self.case_message['functions']

    def execute_case(self):
        if self.case_message['apiList'] is None:
            raise RuntimeError("无法获取API相关数据, 请重试!!!")
        for api_date in self.case_message['apiList']:
            data_collect = ApiCollect()
            data_collect.collect(api_date)
            self.test.defineTrans(api_date['apiId'], api_date['apiName'], api_date['path'])
            step = ApiTestStep(self.test, self.session, data_collect, self.context)  # 定义一个执行步骤控制器
            try:
                if "pre" in data_collect.controller.keys() and data_collect.controller["pre"] is not None:
                    for key in data_collect.controller["pre"]:
                        step.execute_script(key["value"])
                self._reder(step)
                step.execute()
                # 执行后置脚本
                if "postScript" in data_collect.controller.keys() and data_collect.controller["postScript"] is not None:
                    step.execute_script(data_collect.controller["postScript"])
                if step.assert_result['result']:
                    print(f"[{step.collect.apiId}][{step.collect.apiName}]接口断言成功: {step.assert_result['checkMessages']}")
                else:
                    raise AssertionError(step.assert_result['checkMessages'])
            except Exception as e:
                error_info = sys.exc_info()
                if step.collector.controller["errorContinue"].lower() == "true":
                    # 失败后继续执行
                    if issubclass(error_info[0], AssertionError):
                        self.test.recordFailStatus(error_info)
                    else:
                        self.test.recordErrorStatus(error_info)
                else:
                    raise e

    def _reder(self,step):
        if len(step.collect.params) > 0:
            step.collect.params=replace_variables(step.collect.params, self.context, pararm=self.params)
        if step.collect.url is not None:
            step.collect.url=replace_variables(step.collect.url, self.context, pararm=self.params)
        if len(step.collect.body) > 0:
            step.collect.body=replace_variables(step.collect.body, self.context, pararm=self.params,func=self.functions)



