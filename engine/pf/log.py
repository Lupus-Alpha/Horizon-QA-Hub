# -*- coding: utf-8 -*-
import os
import logging
import threading



class LMLogger(object):
    """自定义日志类。日志分两类："""

    def __init__(self, logger_name='Auto Test'):
        self.logger = logging.getLogger(logger_name)    # 生成logger实例
        # 日志输出格式
        self.formatter = logging.Formatter("%(asctime)s - %(levelname)-4s - %(message)s")
        # 指定日志的最低输出级别，默认为WARN级别
        self.logger.setLevel(logging.INFO)

    def get_handler(self, file_path):
        # 生成文件日志的handler
        p, f = os.path.split(file_path)
        if not (os.path.exists(p)):
            os.makedirs(p)  # 判断是否存在该路径，如果不存在就新创建
        file_handler = logging.FileHandler(file_path, encoding="utf8")
        file_handler.setFormatter(self.formatter)
        return file_handler

BASE_PATH = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
LOG_PATH = os.path.join(BASE_PATH, "log")
my_logger = LMLogger()
default_log_path = os.path.join(LOG_PATH, "engine_run.log")
my_lock = threading.RLock()


def DebugLogger(log_info, file_path=default_log_path):
    """debug日志，记录所有日志"""
    try:
        if my_lock.acquire():
            file_handler = my_logger.get_handler(file_path) # 获取处理器
            my_logger.logger.addHandler(file_handler)   # 加入日志处理器
            my_logger.logger.info(log_info) # 写入日志
            my_logger.logger.removeHandler(file_handler)    # 删除日志处理器

            my_lock.release()
    except Exception as e:
        print("Failed to record debug log. Reason:\n %s" % str(e))


def ErrorLogger(log_info, file_path=default_log_path):
    """用以在用例内记录错误日志"""
    try:
        if my_lock.acquire():
            file_handler = my_logger.get_handler(file_path)
            my_logger.logger.addHandler(file_handler)
            my_logger.logger.error(log_info)
            my_logger.logger.removeHandler(file_handler)

            my_lock.release()
    except Exception as e:
        print("Failed to record error log. Reason:\n %s" % str(e))
