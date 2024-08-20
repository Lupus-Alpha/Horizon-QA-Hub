import datetime
import json
import time
from copy import deepcopy
from urllib.parse import urlencode
from core.assertion import Assert
from requests import Session, request

from tools.utils import url_join, extract


class ApiTestStep:
    def __init__(self,test,session,collect,context):
        self.test = test
        self.session = session
        self.collect = collect
        self.context = context
        self.response_headers = None
        self.response_content = None
        self.status_code = None
        self.response_content_bytes = None
        self.response_cookies = None


    def pop_content_type(self):
        pop_key = None
        for key,value in self.collect.others['headers'].items():
            if key.lower() == 'content-type':
                pop_key = key
                break
        if pop_key is not None:
            self.collect.others['headers'].pop(pop_key)

    def execute_script(self,script):
        print("执行脚本",script)
        exec(script)


    def execute(self):
        try:
            self.collect.body = json.loads(self.collect.body)
            if self.collect.body_type == "form-urlencoded":
                self.collect.body['data'] = urlencode(self.collect.body['data'])
            if self.collect.body['file'] is not None:
                    self.pop_content_type()
            url = url_join(self.collect.url,self.collect.path) 
            # if self.collect.controller["sleepBeforeRun"] is not None:
            #     time.sleep(self.collect.controller["sleepBeforeRun"])
            start_time = datetime.datetime.now()
            if "useSession" in self.collect.controller.keys() and self.collect.controller["useSession"].lower() == 'true' and self.collect.controller[
                "saveSession"].lower() == "true":
                res = self.session.request(self.collect.method, url, **self.collect.body)
            elif "useSession" in self.collect.controller.keys() and self.collect.controller["useSession"].lower() == "true":
                session = deepcopy(self.session)  # 只是用session 但不更新session
                res = session.request(self.collect.method, url, **self.collect.body)
            elif "saveSession" in self.collect.controller.keys() and self.collect.controller["saveSession"].lower() == "true":
                session = Session()  # 不用以前的session 但是保存session
                res = session.request(self.collect.method, url, **self.collect.body)
                self.session = session
            else:
                # body = json.loads(self.collect.body)
                res = request(self.collect.method, url, json=self.collect.body.get("json"))
                # self.save_response(res)
            end_time = datetime.datetime.now()
            # 记录接口的请求时间
            self.test.recordTransDuring(int((end_time - start_time).microseconds / 1000))
            # 保存响应结果
            self.save_response(res)
            self.check()
            # 提取依赖参数
            self.extract_depend_params()
        except Exception as e:
            print(e)
        finally:
            if int(self.collect.controller["sleepAfterRun"]) > 0:
                time.sleep(int(self.collect.controller["sleepAfterRun"]))  # 请求后等待

    def check(self):
        '''
        断言
        :return:
        '''
        check_message = list()
        actual = "" # 实际值
        if self.collect.assertions is not None:
            check_result = list()
            for item in self.collect.assertions:
                if item['from'] == 'resCode':
                    actual = self.status_code # 响应状态码，
                elif item['from'] == 'resHeader':
                    actual = extract(item['method'], self.response_headers, item['expression']) # 响应头
                elif item['from'] == 'resBody':
                    actual = extract(item['method'], self.response_content, item['expression']) # 响应体
                else:
                    print('无法在{}位置进行断言'.format(item['from']))
                result, msg = Assert(item['assertion'], actual, item['expect']).compare()
                check_result.append(result)
                check_message.append(msg)
                if not result:
                    break
            final_result = all(check_result)
        else:
            final_result, msg = Assert('相等', self.status_code, 200).compare()
            check_message.append(msg)
        self.assert_result = {
            'apiId': self.collect.apiId,
            'apiName': self.collect.apiName,
            'result': final_result,
            'checkMessages': check_message
        }

    def save_response(self,res):
        '''
        保存响应结果,包括响应头,响应内容,响应状态码,赋给类属性
        :param res:
        :return:
        '''
        self.status_code = res.status_code
        self.response_content_bytes = res.content
        self.response_headers = dict(res.headers)
        try:
            self.response_content = res.json()
        except json.decoder.JSONDecodeError:
            self.response_content = res.text


    def extract_depend_params(self):
        if self.collect.relations is not None:
            for items in self.collect.relations:
                if(items["expression"].lower() == '$'):
                    value = self.response_content_bytes
                elif items['expression'].strip().lower() in ['cookie', 'cookies']:
                    value = self.response_cookies
                else:
                    if items['from'] == 'resHeader':
                        data = self.response_headers
                    elif items['from'] == 'resBody':
                        data = self.response_content
                    elif items['from'] == 'reqHeader':
                        data = self.collect.others['headers']
                    elif items['from'] == 'reqQuery':
                        data = self.collect.others['params']
                    elif items['from'] == 'reqBody':
                        if self.collect.body_type == "json":
                            data = self.collect.others['json']
                        else:
                            data = self.collect.others['data']
                    else:
                        print('无法从{}位置提取依赖参数'.format(items['from']))
                        # raise ExtractValueError('无法从{}位置提取依赖参数'.format(items['from']))
                    value = extract(items['method'], data, items['expression'])
                key = items['name']
                self.context[key] = value




                




