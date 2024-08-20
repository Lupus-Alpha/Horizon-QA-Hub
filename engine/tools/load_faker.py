import os
from faker import Faker
from importlib import import_module, reload
import sys
from faker.providers import BaseProvider
from tools.params_enum import PARAMS_ENUM


class CustomFaker(Faker):
    def __init__(self, package='provider', lm_func=None, *args, **kwargs):
        super().__init__(*args, **kwargs)
        if lm_func is None:
            lm_func = []
        self.package = package
        self.lm_func = lm_func
        self.func_param = PARAMS_ENUM
        self._load_module()

    def __call__(self, name, *args, **kwargs):
        """回调函数"""
        return getattr(self, name)(*args, **kwargs)

    def _read_module(self):
        """加载自定义函数文件"""
        module_path = os.path.join(os.path.dirname(__file__), self.package)  # 获取自定义函数的目录
        module_list = []
        for file_name in os.listdir(module_path):   # 遍历该目录 找到py文件
            if file_name[-2:] == "py":
                module_name = "tools" + "." + self.package + "." + file_name[0:-3]
                module_list.append(module_name)
        return module_list

    def _load_module(self):
        """动态导入自定义的、继承BaseProvider的类"""
        for name in self._read_module():
            """动态加载模块"""
            if name not in sys.modules:  # 全新加载
                module = import_module(name)
            else:  # 刷新模块
                module = sys.modules.get(name)
                reload(module)

            for value in module.__dict__.values():
                if type(value) is type and BaseProvider in value.__bases__:
                    if value.__name__ == "Provider":   # 自定义函数加载进系统provider
                        self._load_lm_func(value)
                    self.add_provider(value)

    def _load_lm_func(self, provider):
        """加载平台自定义函数"""
        for custom in self.lm_func:
            func = provider.lm_custom_func(custom["code"], custom["params"].keys())
            setattr(provider, custom["name"], func)


if __name__=="__main__":
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
    print(fake("a_add_b", 8, 3))
