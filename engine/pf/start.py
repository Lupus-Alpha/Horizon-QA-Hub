import ctypes
import datetime
import inspect
import os
import threading
import time

import psutil
from multiprocessing import Process, Queue, Value

from pf.api import Api
from pf.log import DebugLogger
from pf.report import Report
from pf.task import Task
from pf.upload import Upload
BASE_PATH = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
IMAGE_PATH = os.path.join(BASE_PATH, "image")
class Start(object):
    def __init__(self):

        self.task_status_queue = Queue()
        self.task_queue = Queue()
        self.api = Api()

    def run(self):
        """启动入口"""

        global run_process, report_process
        exec_status = Value("i", 1)
        while True:
            """启动心跳线程"""
            retry = 0
            status_thread = threading.Thread(target=self.send_heartbeat)
            status_thread.start()
            """启动任务线程"""
            task_thread = threading.Thread(target=self.get_task, args=(self.task_queue, self.task_status_queue, exec_status))
            task_thread.start()
            """主进程的主线程循环监听任务通知"""
            while retry < 3:
                if not status_thread.is_alive():
                    print("心跳线程中断关闭任务线程")
                    result = self.stop_thread(task_thread)
                    if result:
                        break
                    else:
                        retry += 1
                        continue
                if not task_thread.is_alive():
                    print("任务线程中断关闭")
                    result = self.stop_thread(status_thread)
                    if result:
                        break
                    else:
                        retry += 1
                        continue
                try:
                    """每1秒获取任务信息"""
                    print("开始获取任务")
                    task = self.task_queue.get(True, 3)
                except Exception:
                    """超时获取不到任务通知继续循环"""
                    continue
                else:
                    print("获取到任务,开始执行")
                    case_result_queue = Queue()  # 执行用例结果通知队列
                    current_exec_status = Value("i", 0)
                    run_process = Process(target=self.run_test, args=(task, case_result_queue, current_exec_status))
                    run_process.start()
                    report_process = Process(target=self.push_result, args=(exec_status, case_result_queue))
                    report_process.start()
                    upload_process = Process(target=self.upload_images, args=(task, current_exec_status)) # 一个进程下执行一个循环，循环中执行任务
                    upload_process.start()
                finally:
                    if exec_status.value == 0:
                        try:
                            """每1秒获取任务状态信息"""
                            task_status = self.task_status_queue.get(True, 1)
                        except Exception:
                            task_status = ""
                        finally:
                            if task_status == "run_all_stop":
                                exec_status.value = 1
                                if run_process.is_alive():
                                    for p in psutil.Process(run_process.pid).children():
                                        p.terminate()
                                    run_process.terminate()
                                if report_process.is_alive() and case_result_queue.qsize() > 0:
                                    time.sleep(30)
                                    if report_process.is_alive():
                                        report_process.terminate()
                            elif not report_process.is_alive():
                                """"如果报告进程中断 中断执行进程"""
                                exec_status.value = 1
                                if run_process.is_alive():
                                    for p in psutil.Process(run_process.pid).children():
                                        p.terminate()
                                    run_process.terminate()
                            elif not run_process.is_alive() and case_result_queue.empty():
                                """"如果执行进程中断 且队列无数据 中断报告进程"""
                                # 当执行进程死掉后 先等待报告上传已有的结果 再去杀掉
                                start_time = datetime.datetime.now()
                                while (datetime.datetime.now() - start_time).seconds < 30:
                                    # 循环等待30s 防止最后一条结果数据没有发送出去 如果报告进程自销则中止等待
                                    if not report_process.is_alive():
                                        break
                                    time.sleep(3)
                                exec_status.value = 1
                                if report_process.is_alive():
                                    report_process.terminate()
                            print("任务已终止")
                            DebugLogger("任务已终止")

    def stop_thread(self,thread):
        try:
            return self._async_raise(thread.ident, SystemExit)
        except:
            return False

    def _async_raise(self,tid, exctype):
        """关闭线程方法"""
        tid = ctypes.c_long(tid)
        if not inspect.isclass(exctype):
            exctype = type(exctype)
        res = ctypes.pythonapi.PyThreadState_SetAsyncExc(tid, ctypes.py_object(exctype))
        if res > 1:  # 结果大于1时 需要重试
            ctypes.pythonapi.PyThreadState_SetAsyncExc(tid, None)
            return False
        elif res == 0:  # 结果等于0时 说明线程早已关闭
            return True
        else:  # 关闭成功
            return True
    def get_task(self, task_queue, task_status_queue, exec_status):
        """获取任务
        task = {'taskId': '24cdd75a-ebb1-4583-ae71-8a45f898dbf3',
        'taskType': 'BATCH',
        'downloadUrl': '/task/file/download/24cdd75a-ebb1-4583-ae71-8a45f898dbf3',
        'reRun': False, 'debugData': None,
        'testCollectionList': [{'collectionId': 'abc4981a-4b35-4480-8c37-07a9c3d244bb',
        'deviceId': None, 'testCaseList': [{'index': 1,
        'caseId': 'cc0fbcc6-4655-4d85-91cc-755eed4f1fbc', 'caseType': 'API'},
         {'index': 2, 'caseId': 'ff667c5e-084f-4483-84c8-50bf3de8876e', 'caseType': 'API'}]}]}

        """
        while True:
            if exec_status.value == 1:
                task = self.api.get_task()
                # print("########",task)
                if task:
                    print("引擎获取任务成功 任务id: %s" % (task["taskId"]))
                    DebugLogger("引擎获取任务成功 任务id: %s" % (task["taskId"]))
                    task_queue.put(task)
                    exec_status.value = 0
            else:

                task_status = self.api.get_task_status()
                if task_status:
                    task_status_queue.put(task_status)
            time.sleep(2)

    def send_heartbeat(self):
        """发送心跳"""
        while True:
            self.api.send_heartbeat()
            time.sleep(10)

    def run_test(self, task, case_result_queue, current_exec_status):
        """执行任务,获取，解析，执行"""
        print("执行任务")
        DebugLogger("开始执行任务 任务id: %s" % task["taskId"])
        s=Task(task)
        plan = s.task_analysis()
        s.create_task(plan, case_result_queue, current_exec_status)

    def push_result(self, exec_status, case_result_queue):
        """上传报告"""
        report = Report(exec_status, case_result_queue)
        report.upload_result()

    def upload_images(self,task, current_exec_status):
        """

        :param task:
        :param current_exec_status:
        :return:
        """
        current_process = psutil.Process(os.getpid())
        task_image_path = os.path.join(IMAGE_PATH, task["taskId"])
        if not os.path.exists(task_image_path):
            os.makedirs(task_image_path)
        while True:
            if current_process.parent() is None:
                current_process.kill()
            files = os.listdir(task_image_path)
            if len(files) > 0:
                print("开始截图上传")
                Upload(files).upload_files(task_image_path)
            else:
                if current_exec_status.value:  # 文件目录为空且执行进程已关闭 销毁图片上传进程
                    os.rmdir(task_image_path)
                    current_process.terminate()
            time.sleep(1)




