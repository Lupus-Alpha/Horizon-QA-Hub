from faker.providers import BaseProvider
import time
import base64
import datetime
import json
from dateutil.relativedelta import relativedelta


class Provider(BaseProvider):

    @staticmethod
    def lm_custom_func(code, params):
        def func(self, *args):
            def sys_return(res):     # 自定义函数code中需要将执行结果通过 sys_return返回
                names["_exec_result"] = res
            names = locals()
            for index, value in enumerate(params):  # 讲一些参数动态加载进局部变量空间中
                names[value] = args[index]
            exec(code)
            return names["_exec_result"]
        return func



    def b64encode_str(self, s: str):
        return base64.b64encode(s.encode('utf-8')).decode()

    def b64encode_bytes(self, s: bytes):
        return base64.b64encode(s).decode()

    def b64encode_file(self, uuid):
        """直接返回uuid对应的文件base64"""
        content = self.loadfile(uuid)
        return base64.b64encode(content).decode()

    def b64decode_toStr(self, s: str):
        return base64.b64decode(s).decode()

    def b64decode_toBytes(self, s: str):
        return base64.b64decode(s)

    def arithmetic(self, expression: str):
        """四则运算"""
        try:
            return eval(expression)
        except Exception:
            raise Exception("四则运算表达式错误:%s" % expression)

    def current_time(self, s: str = '%Y-%m-%d'):
        if s.lower() == "none":
            return int(time.time() * 1000)
        return time.strftime(s)

    def year_shift(self, shift, s: str = '%Y-%m-%d'):
        """年份加减位移，shift位移数值"""
        now_date = datetime.datetime.now()
        shift_date = now_date + relativedelta(years=shift)
        if s.lower() == "none":
            return int(shift_date.timestamp() * 1000)
        return shift_date.strftime(s)

    def month_shift(self, shift, s: str = '%Y-%m-%d'):
        """月份加减位移，shift位移数值"""
        now_date = datetime.datetime.now()
        shift_date = now_date + relativedelta(months=shift)
        if s.lower() == "none":
            return int(shift_date.timestamp() * 1000)
        return shift_date.strftime(s)

    def week_shift(self, shift, s: str = '%Y-%m-%d'):
        """周加减位移，shift位移数值"""
        now_date = datetime.datetime.now()
        delta = datetime.timedelta(weeks=shift)
        shift_date = now_date + delta
        if s.lower() == "none":
            return int(shift_date.timestamp() * 1000)
        return shift_date.strftime(s)

    def date_shift(self, shift, s: str = '%Y-%m-%d'):
        """日期加减位移，shift位移数值"""
        now_date = datetime.datetime.now()
        delta = datetime.timedelta(days=shift)
        shift_date = now_date + delta
        if s.lower() == "none":
            return int(shift_date.timestamp() * 1000)
        return shift_date.strftime(s)

    def hour_shift(self, shift, s: str = '%Y-%m-%d %H:%M:%S'):
        """时间（小时）加减位移 ，shift位移数值"""
        now_date = datetime.datetime.now()
        delta = datetime.timedelta(hours=shift)
        shift_date = now_date + delta
        if s.lower() == "none":
            return int(shift_date.timestamp() * 1000)
        return shift_date.strftime(s)

    def minute_shift(self, shift, s: str = '%Y-%m-%d %H:%M:%S'):
        """时间（分钟）加减位移 ，shift位移数值"""
        now_date = datetime.datetime.now()
        delta = datetime.timedelta(minutes=shift)
        shift_date = now_date + delta
        if s.lower() == "none":
            return int(shift_date.timestamp() * 1000)
        return shift_date.strftime(s)

    def second_shift(self, shift, s: str = '%Y-%m-%d %H:%M:%S'):
        """时间（秒钟）加减位移 ，shift位移数值"""
        now_date = datetime.datetime.now()
        delta = datetime.timedelta(seconds=shift)
        shift_date = now_date + delta
        if s.lower() == "none":
            return int(shift_date.timestamp() * 1000)
        return shift_date.strftime(s)

    def indexof(self, array, index):
        return array[index]

    def keyof(self, map, key):
        return map[key]



    def substing(self, s, start: int=0, end: int=-1):
        return s[start:end]

    def extract(self, data):
        return data

    def replace(self, s, old, new):
        return s.replace(old, new)

    def map_dumps(self, tar):
        return json.dumps(tar)

    def array_dumps(self, tar):
        return json.dumps(tar)
