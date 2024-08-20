from tools.load_faker import CustomFaker

'''
"json": "{"account":"{{@low_str({{$account}})}}","password":"{{new_password}}"}",
"code": "str_target = str_target.lower()\nsys_return(str_target)",
"code": "import time\nprint(1234567)\nsys_return(time.time()*1000)",
'''

import re


def replace_variables(data, context, pararm):
    pattern = r"\"\{\{\s*@(.+?)\(\{\{\$?(.+?)\}\}\)\s*\}\}\""
    match = re.findall(pattern, data)
    if match:
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
            data = re.sub(pattern, fake(func_name, value), data, count=1)

    pattern2 = r"\{\{\$?(\w+)\}\}"
    match2 = re.findall(pattern2, data)
    if match2:
        for i in range(0, len(match2)):
            values = None
            args = match[i][0]
            if args in context:  # 先判断是否是关联参数
                values = context.get(args)
            elif args in pararm:  # 再判断是否是全局参数
                values = pararm.get(args)
            else:
                Exception(f"参数{args}未找到")
            data = re.sub(pattern2, values, data, count=1)


if __name__ == '__main__':
    custom_func = [{
			"code": "str_target = str_target.lower()\nsys_return(str_target)",
			"name": "low_str",
			"params": {
				"str_target": "string"
			}
		}]
    fake = CustomFaker(locale='zh_cn', package='provider', lm_func=custom_func)
    CustomFaker.seed("test")
    request_body = '{"account":"{{@low_str({{$account}})}}","password":"{{new_password}}","account":"{{@low_str({{$accont}})}}"}'
    variables = {"account": "JohnDoe", "new_password": "123456"}
    # 替换变量
    processed_body = replace_variables(request_body)

