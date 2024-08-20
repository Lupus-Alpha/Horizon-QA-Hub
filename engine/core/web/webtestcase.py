import re
import statistics
import sys

from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager

from core.template import JsonTemplate
from core.web.uicollect import UiMsgCollector
from core.web.uistep import UiTestStep
from tools.utils import get_case_message, handle_params_data, handle_operation_data
from selenium import webdriver

class UiTestCase:
    """
    UI测试用例执行
    """
    def __init__(self,test):
        self.test = test
        self.case_message = get_case_message(test.test_data)
        self.driver = self._before_execute()
        self.context = test.context # 存储依赖参数
        self.functions = self.case_message['functions']  # 自定义函数
        self.function = self.case_message['functions'] # 自定义函数
        self.params = handle_params_data(self.case_message['params'])
        self.template = JsonTemplate(self.context, self.functions, self.params)  # 模板实例
        self.comp = re.compile(r"\{\{.*?\}\}")


    def execute(self):
        if self.case_message['optList'] is None:
            self._after_excute()
            raise RuntimeError("无法获取WEB测试数据, 请重试!!!")
        step_count = len(self.case_message['optList'])
        step_n = 0
        try:
            while step_n < step_count:
                opt_content = self.case_message['optList'][step_n]
                self.test.defineTrans(opt_content["operationId"], opt_content['operationTrans'],self._get_opt_content(opt_content['operationElement']))
                collector = UiMsgCollector()
                collector.collect(opt_content)  # 收集执行用到的数据，比如操作元素，数据，类型等
                step = UiTestStep(self.test, self.driver, collector) # 定义一个执行类
                self._render(step)  # 替换操作过程的{{}}变量
                step.execute()
                # self._assert_solve(step)
                step_n += 1
        except Exception as e:
                # 非AssertionError截图
            if not isinstance(e, AssertionError):
                self.test.saveScreeShot(self.driver.get_screenshot_as_png())
            raise e
        finally:
            self._after_excute()


    @staticmethod
    def _get_opt_content(opt_content):
        content = ""
        if opt_content is not None:
            for key, element in opt_content.items():
                content = "%s\n %s: %s" % (content, key, element["target"])
        return content

    def _render(self, step):
        """渲染函数"""
        if step.collector.opt_data is not None:
            for expr, param in step.collector.opt_data.items(): # 数据名 数据值
                param_value = param["value"]  # {{}}
                if isinstance(param_value, str) and self.comp.search(param_value) is not None:
                    self.template.init(param_value)
                    render_value = self.template.render()
                    param["value"] = render_value
            step.collector.opt_data = handle_operation_data(step.collector.opt_data)

    def _before_execute(self):
        if self.test.driver["browser_opt"] == "headless": # 判断是否是无界面模式
            opt = webdriver.ChromeOptions()
            opt.add_argument("--headless")
            opt.add_argument("--no-sandbox")
        else:
            opt = webdriver.ChromeOptions()
            opt.add_experimental_option('excludeSwitches', ['enable-logging'])
        # 初始化浏览器
        old_driver = self.test.driver["driver"]
        if self.case_message["startDriver"]:
            if old_driver is not None:
                old_driver.quit()
            self.test.driver["driver"] = None  # 并清空原有的浏览器实例
            service = Service(ChromeDriverManager().install())
            return webdriver.Chrome(service=service, options=opt)
        else:
            if old_driver is not None:
                return old_driver
            else:
                raise RuntimeError("无法找到已启动的浏览器进程 请检查用例开关浏览器配置")

    def _after_excute(self):
        if self.case_message["closeDriver"]:
            self.driver.quit() # 关闭浏览器
        else:  # 保留浏览器
            self.test.driver["driver"] = self.driver

    def _assert_solve(self, step):
        """断言处理"""
        if step.collector.opt_type == "assertion":
            # 断言成功
            if step.result[0]:
                self.test.debugLog('[{}][{}]断言成功: {}'.format(step.collector.id,
                                                                 step.collector.opt_name,
                                                                 step.result[1]))
            else:  # 断言失败
                self.test.errorLog('[{}][{}]断言失败: {}'.format(step.collector.id,
                                                                 step.collector.opt_name,
                                                                 step.result[1]))
                # 截图
                self.test.saveScreenShot(step.collector.opt_trans, self.driver.get_screenshot_as_png())
                # 是否继续
                if "continue" in step.collector.opt_data and step.collector.opt_data["continue"] is True:
                    try:
                        raise AssertionError(step.result[1])
                    except AssertionError:
                        error_info = sys.exc_info()  # 捕获抛出的异常
                        self.test.recordFailStatus(error_info)
                else:
                    raise AssertionError(step.result[1])

