import copy
import json
import re
import time

import jsonpath

from tools.load_faker import CustomFaker

def handle_operation_data(data):
    """处理操作数据"""
    result = {}
    for key, item in data.items():
        data_type = item["type"]
        data_value = item["value"]
        try:
            if data_type == "JSONObject":
                data_value = eval(data_value)
            elif data_type == "JSONArray":
                data_value = eval(data_value)
            elif data_type == "Boolean":
                if data_value.lower() == "true":
                    data_value = True
                else:
                    data_value = False
            elif data_type == "Int":
                data_value = int(data_value)
            elif data_type == "Float":
                data_value = float(data_value)
            else:
                data_value = data_value
        except:
            pass
        result[key] = data_value
    return result

def handle_files(files):
    for items in files:
        pass

def quotation_marks(s):
    """处理字符串前后的引号（中英文）"""
    # 前引号
    if s[0] in ["'", '"', b'\xe2\x80\x98'.decode('utf-8'), b'\xe2\x80\x99'.decode('utf-8'),
                b'\xe2\x80\x9c'.decode('utf-8'), b'\xe2\x80\x9d'.decode('utf-8')]:
        before = 1
    elif s[0:2] in ["\\'", '\\"']:
        before = 2
    else:
        return s
    # 后引号, 先判断转义的，在判断单个引号
    if s[-2:] in ["\\'", '\\"']:
        after = -2
    elif s[-1] in ["'", '"', b'\xe2\x80\x98'.decode('utf-8'), b'\xe2\x80\x99'.decode('utf-8'),
                   b'\xe2\x80\x9c'.decode('utf-8'), b'\xe2\x80\x9d'.decode('utf-8')]:
        after = -1
    else:
        return s
    return s[before:after]


def handle_form_data(form):
    form_data = {}
    form_file = {}
    for item in form:
        try:
            if item["type"] == "File":
                form_file[item["name"]] = "{{@loadfile(%s)}}" % item["value"]
            elif item["type"] == "JSONObject":
                form_data[item["name"]] = eval(item["value"])
            elif item["type"] == "JSONArray":
                form_data[item["name"]] = eval(item["value"])
            elif item["type"] == "Boolean":
                if item["value"].lower() == 'true':
                    form_data[item["name"]] = True
                else:
                    form_data[item["name"]] = False
            elif item["type"] == "Int":
                form_data[item["name"]] = int(item["value"])
            elif item["type"] == "Float":
                form_data[item["name"]] = float(item["value"])
            else:
                form_data[item["name"]] = item["value"]
        except:
            form_data[item["name"]] = item["value"]
    return form_data, form_file

def handle_params_data(params):
    """处理公参数据"""
    result = {}
    for key, item in params.items():
        data_type = item["type"]
        data_value = item["value"]
        try:
            if data_type == "JSONObject":
                data_value = eval(data_value)
            elif data_type == "JSONArray":
                data_value = eval(data_value)
            elif data_type == "Boolean":
                if data_value.lower() == "true":
                    data_value = True
                else:
                    data_value = False
            elif data_type == "Int":
                data_value = int(data_value)
            elif data_type == "Float":
                data_value = float(data_value)
        except:
            pass
        result[key] = data_value
    return result


def get_case_message(data):
    if isinstance(data, dict):
        return data
    else:
        with open(data, 'rb') as f:
            return json.load(f) #json.load()方法可以直接从文件对象中读取内容并解析为JSON格式的数据


def url_join(url: str ,path: str):
    if url !='':
        url = url if url.endswith('/') else url + '/'
        path = path[1:] if path.startswith('/') else path
        return url+path
    return path


def json_to_path(data):
    """扁平化json"""
    queue = [("_REQUEST_BODY", data)]
    fina = {}
    while len(queue) != 0:
        (path, tar) = queue.pop()
        if len(tar) == 0:
            fina["%s" % path] = tar
        if isinstance(tar, dict):
            for key, value in tar.items():
                try:
                    if key.isdigit():
                        key = "'%s'" % str(key)
                except:
                    key = "'%s'" % str(key)
                if isinstance(value, dict) or isinstance(value, list):
                    queue.append(("%s.%s" % (path, key), value))
                else:
                    fina["%s.%s" % (path, key)] = value
        else:
            for index, value in enumerate(tar):
                if isinstance(value, dict) or isinstance(value, list):
                    queue.append(("%s[%d]" % (path, index), value))
                else:
                    fina["%s[%d]" % (path, index)] = value
    return fina

def get_json_relation(data: dict, data_from: str):
    """先扁平化json, 在按照渲染顺序进行排序"""
    return relate_sort(json_to_path(data), data_from)

def relate_sort(data, data_from):
    """关联排序"""
    not_relate_list = []
    relate_list = []
    for key, value in data.items():
        """对json进行分组 需要排序的一组 不需要的另一组"""
        if "#{" in str(value):
            relate_list.append((key.replace("_REQUEST_BODY", "$"), value))
        else:
            not_relate_list.append((key.replace("_REQUEST_BODY", "$"), value))
    copy_list = copy.deepcopy(relate_list)
    sorted_list = []
    for index in range(len(relate_list)):
        """根据关联关系进行排序"""
        for (key, value) in copy_list:
            for (com_key, com_value) in copy_list:
                if com_key[0:2] == "$.":
                    json_path = com_key[2:]
                else:
                    json_path = com_key[1:]
                if json_path in str(value) and com_key != key:
                    break
            else:
                sorted_list.append((key, value))
                copy_list.remove((key, value))
                break
    for (key, value) in sorted_list:
        """如果有需要全部参数的放在最后"""
        if data_from == "query":
            sign = "#{_REQUEST_QUERY}"
        else:
            sign = "#{_REQUEST_BODY}"
        if sign in str(value):
            sorted_list.remove((key, value))
            sorted_list.append((key, value))
            break
    # print(sorted_list)
    return not_relate_list + sorted_list


def extract_by_jsonpath(api_data: dict, expression: str):
    """
    jsonpath形式的数据提取
    :param api_data: 待提取的json数据
    :param expression: 表达式
    :return:
    """
    # exp = expression if expression.startswith('$') else '$.' + expression  jsonpath不一定需要'$.'前缀
    value = jsonpath.jsonpath(api_data, expression) # 默认返回的是列表
    if value:
        return value[0] if len(value) == 1 else value
    else:
        raise ExtractValueError('jsonpath表达式错误: {}'.format(expression))


def replace_variables(data, context, pararm, func=None):
    pattern = r"\"\{\{\s*@(.+?)\(\{\{\$?(.+?)\}\}\)\s*\}\}\""
    if isinstance(data, dict):
        data = json.dumps(data, ensure_ascii=False)
    match = re.findall(pattern, data)
    if func is not None and len(match) >0:
        for i in range(0, len(match)):
            value = None
            func_name = match[i][0]  # 函数名
            arguments = match[i][1]  # 参数
            if arguments in context:  # 先判断是否是关联参数
                value = context.get(arguments)
            elif arguments in pararm:  # 再判断是否是全局参数
                value = pararm.get(arguments)
            else:
                Exception(f"参数{arguments}未找到")

            faker = CustomFaker(locale='zh_cn', package='provider', lm_func=func)
            CustomFaker.seed(str(time.time()))
            data = re.sub(pattern, faker(func_name, value), data, count=1)

    pattern2 = r"\{\{\$?(\w+)\}\}"
    match2 = re.findall(pattern2, data)
    if len(match2) >0:
        for i in range(0, len(match2)):
            values = None
            args = match2[i]
            if args in context:  # 先判断是否是关联参数
                values = context.get(args)
                values = values.get("value")
            elif args in pararm:  # 再判断是否是全局参数
                values = pararm.get(args)
                values = values.get("value")
            else:
                Exception(f"参数{args}未找到")
            data = re.sub(pattern2, values, data, count=1)
    return data


def extract(method,data,expression):
    if method == 'jsonpath':
        data = jsonpath.jsonpath(data, expression)
        data= data[0] if len(data) ==1 else data
        return data
    elif method == 'regular':
        content = json.dumps(data, ensure_ascii=False)
        result = re.findall(expression, content)
        if len(result) > 0:
            return result[0] if len(result) == 1 else result
        else:
            print("正则表达式匹配失败: {}".format(expression))

    else:
        print("未定义提取函数: {}".format(method))



class ExtractValueError(Exception):
    """提取值失败"""

class NotExistedController(Exception):
    """不存在的控制器"""