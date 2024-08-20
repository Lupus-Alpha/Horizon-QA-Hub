import json
from tools.utils import handle_form_data, handle_files


class ApiCollect:
    def __init__(self):
        self.apiId = None
        self.apiName = None
        self.method = None
        self.url = None
        self.path = None
        self.protocol = None
        self.body_type = None
        self.params = {}
        self.body = {}
        self.others = {}
        self.controller = {}
        self.assertions = []
        self.relations = []
    def collect_flag(self,atr,data):
        if data[atr] is None:
            print(atr,"此项数据不存在")
        elif len(data[atr])==0:
            print(data+"中"+atr,"数据为空")
        else:
            setattr(self, atr, data[atr])



    def collect(self,api_date):
        init_attributes = self.__dict__.keys()
        for key in api_date.keys():
            if key in init_attributes:
                self.collect_flag(key,api_date)

        self.collect_query(api_date)
        self.collect_body(api_date)
        self.collect_headers(api_date)


    def collect_query(self, api_data):
        if len(api_data["query"]) > 0:
            self.others["query"] = api_data["query"]
        else:
            self.others["query"] = None

    def collect_body(self,api_date):

        body = api_date["body"]
        body_type = body.get("type")
        self.body_type = body_type
        if body_type == "json":
            # if api_date["body_type"] != '':
            self.body["json"] = body["json"]
        elif body_type in ("form-urlencoded", "form-data"):
            body_data, body_file = handle_form_data(body["form"])
            if len(body_data) > 0:
                self.body["data"] = body_data
            if len(body_file) > 0:
                self.body["files"] = body_file
        elif body["type"] in ("text", "xml", "html"):
            if body["raw"] != "":
                self.body["data"] = body["raw"]
        elif body["type"] == "file":
            files = handle_files(body["file"])#很多文件时需要遍历文件
            if len(files) > 0:
                self.body["files"] = files

    def collect_headers(self, api_data):
        self.collect_other(api_data, 'headers')
        if self.others['headers'] is None:  # 请求头为空
            self.others['headers'] = {'Content-Type': 'application/json;charset=UTF-8'}
        elif 'content-type' not in [key.lower() for key in self.others['headers'].keys()]:  # Content-Type不在请求头
            self.others['headers']['Content-Type'] = 'application/json;charset=UTF-8'
        else:  # 请求头有不是空，且content-type字段在请求头里面
            for key, value in self.others['headers'].items():  # 遍历字典，找到content-type
                if key.lower() == 'content-type':
                    if value is None or len(value) == 0:  # 如果值为空或者长度为0
                        self.others['headers'][key] = 'application/json;charset=UTF-8'
                    break  # 只可能有一个content-type, 跳出循环

    def collect_other(self, api_data, arg_name, func=lambda x: x):
        if arg_name not in api_data or api_data[arg_name] is None or len(api_data[arg_name]) == 0:
            self.others[arg_name] = None
        else:
            self.others[arg_name] = func(api_data[arg_name])
