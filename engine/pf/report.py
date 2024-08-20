import time

from pf.api import Api


class Report(object):
    def __init__(self, exec_status, case_result_queue):
        self.case_result_queue = case_result_queue
        self.status = exec_status
        self.api = Api()

    def upload_result(self):
        global task_id
        test_result = []
        while True:
            try:
                message = self.case_result_queue.get()
            except Exception as e:
                print("获取执行结果报错 错误信息%s" % str(e))
            else:
                if isinstance(message, str):
                    if "run_all_start" in message:
                        print("任务执行启动 开始监听执行结果")
                        task_id = message.split("--")[1]
                    elif "run_all_stop" in message:
                        if len(test_result) != 0:
                            self.api.upload_result(task_id, test_result)
                        self.post_stop(task_id)  # 执行结束
                        self.status.value = 1  # ce
                        time.sleep(2)
                        break
                    else: # 强制停止时，直接上传结果，并清空结果队列
                        if (len(test_result) != 0):
                            self.api.upload_result(task_id, test_result)
                            test_result.clear()
                else:
                    test_result.append(message)
                    if len(test_result) > 5:
                        self.api.upload_result(task_id, test_result)
                        test_result.clear()

    def post_stop(self, task_id):
        print("任务执行结束 调用接口通知平台")
        self.api.complete_task(task_id)
