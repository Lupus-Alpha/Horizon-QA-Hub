from functools import reduce
from hashlib import md5
from jsonpath import jsonpath
from jsonpath_ng.parser import JsonPathParser
import json
import re
import time

from tools import get_func_lib
from tools.utils import extract_by_jsonpath, quotation_marks

# 需要在渲染过程中，被替换的对象类型
CTYPES = ['WebElement', 'Alert', ]


"""渲染模版的作用就是将一段带有{{}}的数据替换成函数或者变量中的值 需要满足该占位符写在任意的位置 且支持嵌套渲染"""
class JsonTemplate:
    """json模板渲染"""

    def __init__(self, context, functions, params, variable_start_string='{{', variable_end_string='}}', function_prefix='@', param_prefix='$'):
        self.param_prefix = param_prefix
        self.data = None  # 接口的数据，要是字符串类型
        self.context = context  # 依赖参数的字典
        self.params = params    # 公共参数
        # 需要被替换的参数前后缀
        self.variable_start_string = variable_start_string
        self.variable_end_string = variable_end_string
        # 函数前缀
        self.function_prefix = function_prefix
        # 公参前缀
        self.param_prefix = param_prefix
        # 用列表模拟栈，存储经过渲染后的字符
        self.stack = list()
        # 存储函数需要使用的信息，比如request_body，这里必须是引用，因为渲染过程必定改变body，函数要用到实时更新的数据
        self.request_path = None
        self.request_headers = None
        self.request_query = None
        self.request_body = None
        # 初始化函数库 返回实例
        self.func_lib = get_func_lib(functions)
        # 字节型数据存储
        self.bytes_map = dict()
        # json解析器
        self.parser = JsonPathParser()

    def init(self, data): # data 可以是json 也可以是str
        """数据初始化，创建实例后必须调用"""
        self.data = json.dumps(data, ensure_ascii=False)
        self.stack.clear()
        self.bytes_map.clear()

    def set_help_data(self, path: str, headers: dict, query: dict, body: dict):
        """设置origin"""
        self.request_path = path
        self.request_headers = headers
        self.request_query = query
        self.request_body = body

    def render(self):
        """渲染的具体实现函数"""
        """ {"a": "{{@func({{name}})}}" """
        # 用列表模拟栈，存储匹配到的variable_start_string位置
        start_stack = list()
        # variable_start_string的长度
        start_length = len(self.variable_start_string)  #2
        # variable_end_string的长度
        end_length = len(self.variable_end_string)
        # stack的栈顶位置
        top = 0
        # 去除标志
        flag = False
        # 开始栈处理
        for cur in range(len(self.data)):
            self.stack.append(self.data[cur])
            top += 1
            if flag:
                self.stack.pop()
                top -= 1
                flag = False
                continue
            # 符合variable_start_string的位置入栈
            if reduce(lambda x, y: x + y, self.stack[-start_length:]) == self.variable_start_string:    # 判断最后两个字符是否等于{{
                start_stack.append(top - start_length)  # 记录当前栈开始的值
            # 符合variable_end_string时，从start_stack获取需要替换字符串的开始位置，pop栈元素至此处
            if reduce(lambda x, y: x + y, self.stack[-end_length:]) == self.variable_end_string:    # 找到第一个}}时 则需要开始处理
                if len(start_stack) == 0:
                    continue
                recent = start_stack.pop()  # 最近入栈的
                tmp = ''  # 临时变量存储已经闭合的字符串
                for _ in range(top - recent):
                    tmp += self.stack.pop() # }}EMAN{{
                    top -= 1
                # 判断{{ xxx }}前后是否有“
                if self.stack[-1] == '"' and self.data[cur + 1] == '"':
                    self.stack.pop()
                    top -= 1
                    flag = True
                else:
                    flag = False
                # 翻转字符串
                tmp = tmp[::-1] # {{name}}
                # 删除前后的标识字符串和一些无用空格
                key = tmp[start_length:-end_length].strip() # name
                # env存在key
                if key in self.context: # 先判断是否是关联参数
                    # 要替换进去的值
                    value = self.context.get(key)
                elif key.startswith(self.param_prefix) and key[1:] in self.params:  # 再判断是否公共参数
                    value = self.params.get(key[1:])
                elif key.startswith(self.function_prefix):  # 再判断是否函数 {{@func(1,2)}}
                    name_args = self.split_func(key, self.function_prefix)
                    name_args = [_ for _ in map(self.replace_param, name_args)]# [func, 1, 2]
                    value = self.func_lib(name_args[0], *name_args[1:])
                else:
                    # 变量不存在或者函数不存在时抛异常，而不是延迟到请求执行后
                    raise NotExistedVariableOrFunctionError('不存在的公参、关联变量或取值函数: {}'.format(key))
                # json格式化
                if not flag and isinstance(value, str):
                    if '"' in value:
                        value = json.dumps(value)[1:-1]
                    final_value = value
                elif isinstance(value, bytes):
                    final_value = self._bytes_save(value, flag) # {"a": "bytes_md5"
                elif isinstance(value, list):
                    final_value = list()
                    for list_item in value:
                        if isinstance(list_item, bytes):
                            final_value.append(self._bytes_save(list_item, False))
                        else:
                            final_value.append(list_item)
                    final_value = json.dumps(final_value)
                else:
                    final_value = json.dumps(value)
                for s in final_value:   # 将所有处理完成的值按一个一个字符加入到stack列表中
                    self.stack.append(s)
                    top += 1
        res = json.loads(reduce(lambda x, y: x + y, self.stack))    # 还原data数据
        # 如果有字节数据
        if len(self.bytes_map) > 0:
            pattern = r'#\{(bytes_\w+_\d+?)\}'
            if isinstance(res, str):
                bytes_value = self._bytes_slove(res, pattern)
                if bytes_value is not None:
                    res = bytes_value
            elif isinstance(res, dict) or isinstance(res, list):
                for i, j in zip(jsonpath(res, '$..'), jsonpath(res, '$..', result_type='PATH')):
                    # [(json节点的值, json节点的jsonpath表达式),()...]
                    if isinstance(i, str):
                        bytes_value = self._bytes_slove(i, pattern)
                        if bytes_value is not None:
                            expression = self.parser.parse(j)
                            expression.update(res, bytes_value)
            else:
                # 强迫症，写不写这个else都行
                pass
        return res

    def _bytes_save(self, value, flag):
        # 将字节数据存到self.bytes_map中，并且用self.bytes_map的key代替写入self.stack
        # 避免字节型数据不能被json解析的问题
        bytes_map_key = 'bytes_{}_{}'.format(md5(value).hexdigest(), int(time.time() * 1000000000))
        self.bytes_map[bytes_map_key] = value
        change_value = '#{%s}' % bytes_map_key
        if flag:
            final_value = json.dumps(change_value)
        else:
            final_value = change_value
        return final_value

    def _bytes_slove(self, s, pattern):
        search_result = re.search(pattern, s)
        if search_result is not None:
            expr = search_result.group(1)
            return self.bytes_map[expr]

    def replace_param(self, param):
        """将代表函数库参数字符串替换未实际的值"""
        if not isinstance(param, str):
            return param
        search_result = re.search(r'#\{(.*?)\}', param)
        if search_result is not None:
            expr = search_result.group(1).strip()
            if expr.lower() == '_request_path':
                return self.request_path
            elif expr.lower() == '_request_header':
                return self.request_headers
            elif expr.lower() == '_request_body':
                return self.request_body
            elif expr.lower() == '_request_query':
                return self.request_query
            elif expr.startswith('bytes_'):
                return self.bytes_map[expr]
            elif expr.startswith('objects_'):
                return self.objects_map[expr]
            else:
                if not expr.startswith('$'):
                    expr = '$.' + expr
                return extract_by_jsonpath(self.request_body, expr)
        else:
            return param

    def split_func(self, statement: str, flag: 'str' = '@'):
        """
        将函数的声明拆分为函数名和对应的参数
        :param statement:
        :param flag:
        :return:
        """
        pattern = flag + r'([_a-zA-Z][_a-zA-Z0-9]*)(\(.*?\))?'
        m = re.match(pattern, statement)
        result = list()
        if m is not None:
            name, args = m.groups()
            result.append(name)
            if args is not None and args != '()':
                argList = [arg.strip() for arg in args[1:-1].split(',')]
                argList_length = len(argList)
                if not (argList_length == 1 and len(argList[0]) == 0):
                    if name not in self.func_lib.func_param:
                        for i in range(argList_length):
                            result.append(argList[i])
                    else:
                        type_list = self.func_lib.func_param[name]
                        j = 0
                        for i in range(len(type_list)):
                            if j >= argList_length:
                                break
                            if type_list[i] is str:
                                result.append(quotation_marks(argList[j]))
                                j += 1
                            elif type_list[i] is int:
                                result.append(int(argList[j]))
                                j += 1
                            elif type_list[i] is float:
                                result.append(float(argList[j]))
                                j += 1
                            elif type_list[i] is bool:
                                result.append(False if argList[j].lower() == 'false' else True)
                                j += 1
                            elif type_list[i] is dict:
                                j, r = self.concat(j, argList, '}')
                                result.append(r)
                            elif type_list[i] is list:
                                j, r = self.concat(j, argList, ']')
                                result.append(r)
                            elif type_list[i] is bytes:
                                result.append(argList[j])
                                j += 1
                            elif type_list[i] is None:  # 其他类型参数不做任何处理 直接透传
                                result.append(argList[j])
                                j += 1
                            else:
                                raise SplitFunctionError('函数{}第{}个参数类型错误: {}'.format(name, i + 1, type_list[i]))
            return result
        else:
            raise SplitFunctionError('函数错误: {}'.format(statement))

    @staticmethod
    def concat(start: int, arg_list: list, terminal_char: str):
        """从arg_list中拼接json数据（列表、字典）"""
        # 找到最后的terminal_char位置
        end = -1
        length = len(arg_list)
        for i in range(start, length):
            if terminal_char in arg_list[i] and i > end:
                end = i
        s = reduce(lambda x, y: x + ',' + y, arg_list[start:end + 1])
        r = json.loads(quotation_marks(s))
        if isinstance(r, str):
            r = json.loads(r)
        return end + 1, r


class NotExistedVariableOrFunctionError(Exception):
    """不存在的应用变量或者函数"""


class SplitFunctionError(Exception):
    """函数处理错误"""


t = JsonTemplate({"name": 1}, None, {})
t.init("{{name}}")
res = t.render()
print(res)
