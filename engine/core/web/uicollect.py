class UiMsgCollector:
    def __init__(self):
        self.id = None
        self.opt_type = None
        self.opt_name = None
        self.opt_trans = None
        self.opt_element = None
        self.opt_data = None
        self.opt_code = None


    @staticmethod
    def __parse(ui_data: dict, name):
        """字段name在ui_data中的防呆处理"""
        if name not in ui_data:
            return None
        return ui_data.get(name)


    def collect_opt_element(self, ui_data):
        """收集操作元素"""
        opt_element = UiMsgCollector.__parse(ui_data, "operationElement")
        if opt_element is None or len(opt_element) == 0:
            self.opt_element = None
        else:
            for name, element in opt_element.items():
                opt_element[name] = (element["by"].lower(), element["expression"])
                # 最终生成的是: {元素名称: (定位方式: 表达式)}
            self.opt_element = opt_element


    def collect(self, ui_data):
        """统一收集函数"""
        self.opt_data = UiMsgCollector.__parse(ui_data, "operationData")
        self.opt_code = UiMsgCollector.__parse(ui_data, "operationCode")
        self.opt_trans = UiMsgCollector.__parse(ui_data, "operationTrans")
        self.opt_name = UiMsgCollector.__parse(ui_data, "operationName")
        self.opt_type = UiMsgCollector.__parse(ui_data, "operationType")
        self.id = UiMsgCollector.__parse(ui_data, "operationId")
        self.opt_element = self.collect_opt_element(ui_data)
