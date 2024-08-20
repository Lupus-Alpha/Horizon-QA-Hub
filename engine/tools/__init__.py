"""
函数调用方式
from funclib import get_func_lib
faker = get_func_lib()
value = faker('func name', param1, param2...)
"""
from .load_faker import CustomFaker
import time


def get_func_lib(lm_func=None):
    """获取函数库实例"""
    faker = CustomFaker(locale='zh_cn', package='provider', lm_func=lm_func)
    CustomFaker.seed(str(time.time()))
    return faker

