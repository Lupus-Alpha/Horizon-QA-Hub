from tools.load_faker import CustomFaker

result = {}
def lm_custom_func(code, params):
    def func(*args):
        def sys_return(res):
            names["exec_res"] = res
        names = locals()
        for index, value in enumerate(params):
            names[value] = args[index]
        exec(code)
        return names["exec_res"]  # 返回执行结果

    return func
if __name__ == '__main__':
    custom_func = [
        {
            "name": "a_add_b",
            "code": u"""sys_return(a+b)""",
            "params": {
                "a": "int",
                "b": "int"
            }
        }
    ]
    fake = CustomFaker(locale='zh_cn', package='provider', lm_func=custom_func)
    CustomFaker.seed("test")
    # code = u"str_target = str_target.lower()\nsys_return(str_target)"
    # func_name = lm_custom_func(code, params={'str_target': "string"})
    # func_name("AncdB")

