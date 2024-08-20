from core.web.install import *
from tools.utils import NotExistedController


class UiTestStep:
    def __init__(self, test, driver, collector):
        self.test = test
        self.driver = driver
        self.collector = collector
        self.result = None  # 测试结果

    def execute(self):
        '''
        根据不同的操作类型，执行不同的操作，最后按照相应的操作名去执行操作
        :return:
        '''
        try:
            self.test.debugLog('[{}]UI操作[{}]开始'.format(self.collector.id, self.collector.opt_name))
            opt_type = self.collector.opt_type
            if opt_type == "browser":
                func = install_browser_opt(self.collector.opt_name)
            elif opt_type == "page":
                func = install_page_opt(self.collector.opt_name)
            elif opt_type == "condition":
                func = install_condition_opt(self.collector.opt_name)
            elif opt_type == "assertion":
                func = install_assert_opt(self.collector.opt_name)
            elif opt_type == "relation":
                func = install_relate_opt(self.collector.opt_name)
            else:
                func = install_scenario_opt(self.collector.opt_name)
            if func is None:
                raise NotExistedController("未定义控件")
            opt_content = {
                "trans": self.collector.opt_trans,  # 事务名  如登录
                "code": self.collector.opt_code,  # 自定义的代码
                "element": self.collector.opt_element,  # 操作元素的集合
                "data": self.collector.opt_data  # 操作数据的集合
            }
            self.result = func(self.test, self.driver, **opt_content)  # 传测试用例实例 浏览器驱动实例
            # 日志记录

        finally:
            # 日志记录：接口执行结束
            self.test.debugLog('[{}]UI操作[{}]结束'.format(self.collector.id, self.collector.opt_name))
