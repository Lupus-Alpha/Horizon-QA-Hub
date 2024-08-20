import base64
import json
import time

import requests

from config.config import *
from pf.log import DebugLogger, ErrorLogger


class Api(object):
    def __init__(self):
        self.engine_code = engine_code
        self.engine_secret = engine_secret
        self.plate_form_url = plate_form_url
        self.headers = {
            "Content-Type": "application/json",
            "token": self.apply_token()
        }
    def get_task(self):
        """"获取任务"""
        url = self.plate_form_url + "/engine/task/fetch"
        for index in range(2):  # 失败重试机制
            data = {
                "engineCode": self.engine_code,
                "timestamp": int(time.time())
            }
            try:
                if index > 0:
                    DebugLogger("-------重试调用获取引擎任务接口--------")
                res = requests.post(url, json=data, headers=self.headers)
                if res.status_code == 200:
                    status = res.json()["status"]
                    if status == 0:
                        return res.json()["data"]
                    elif status in (1002, 1003, 1004):
                        DebugLogger("token校验错误 重新申请token")
                        self.apply_token()
                        continue
                    else:
                        DebugLogger("获取引擎任务请求失败")
                else:
                    DebugLogger("调用获取引擎任务接口 响应状态为：%s" % res.status_code)
            except Exception as e:
                ErrorLogger("调用获取引擎任务接口 发生错误 错误信息为：%s" % e)
            break

    def get_task_status(self):
        pass

    def apply_token(self):
        url = plate_form_url + "/engine/token/apply"
        data = {
            "engineCode": self.engine_code,
            "engineSecret": self.engine_secret,
            "timestamp": int(time.time()),
        }
        try:
            res = requests.post(url=url, json=data)
            return res.json()["data"]
        except Exception as e:
            print(e)

    def send_heartbeat(self):
        url= plate_form_url + "/engine/heartbeat/send"
        data = {
            "engineCode": self.engine_code,
            "timestamp": int(time.time())
        }
        for i in range(3):
            try:
                res = requests.post(url, json=data, headers=self.headers)
                if res.status_code == 200:
                    status = res.json()["status"]
                    if status == 0:
                        print("心跳发送成功")
                        return True
                    elif status in (1002, 1003, 1004):
                        self.apply_token()
                        print("重新申请token")
                        continue
                else:
                    print("心跳发送失败")
                    return False
            except Exception as e:
                print(e)


    def download_task_file(self, url):
        dowload_url = self.plate_form_url + url
        try:
            res = requests.get(dowload_url, headers=self.headers, stream=True)
            if res.status_code == 200:
                    print("下载任务文件成功")
                    return res
            else:
                print("下载文件失败")
                print(res.status_code)
                return False
            return res
        except Exception as e:
            ErrorLogger("下载任务文件失败 错误信息: %s" % e)
            return None

    def complete_task(self, task_id):
        """"反馈任务结束"""
        url = self.plate_form_url + "/engine/task/complete"
        data = {
            "engineCode": self.engine_code,
            "timestamp": int(time.time()),
            "taskId": task_id
        }
        for index in range(2):
            try:
                res = requests.post(url, json=data, headers=self.headers)
                if res.status_code == 200:
                    status = res.json()["status"]
                    if status == 0:
                        print("上传任务结果成功")
                        return True
                    elif status in (1002, 1003, 1004):
                        self.headers = {
                            "Content-Type": "application/json",
                            "token": self.apply_token()
                        }
                        continue
                else:
                    print("反馈任务结果失败")
                    return False
            except Exception as e:
                print(e)
        return False

    def upload_result(self, task_id, test_result):
        url = self.plate_form_url + "/engine/result/upload"
        data = {
            "engineCode": self.engine_code,
            "taskId": task_id,
            "timestamp": int(time.time()),
            "caseResultList": test_result
        }
        for index in range(2):
            try:
                res = requests.post(url, json=data, headers=self.headers)
                if res.status_code == 200:
                    status = res.json()["status"]
                    if status == 0:
                        print("上传任务结果成功")
                        return True
                    elif status in (1002, 1003, 1004):
                        self.headers = {
                            "Content-Type": "application/json",
                            "token": self.apply_token()
                        }
                        continue
                else:
                    print("上传任务结果失败")
                    return False
            except Exception as e:
                print(e)
        return False

    def upload_image(self,image_path, uuid):
        url = self.plate_form_url + "/engine/image/upload"
        data = {
            "engineCode": self.engine_code,
            "fileName": "%s.png"%uuid,
            "timestamp": int(time.time())
        }
        with open(os.path.join(image_path, "%s.png" % uuid),"rb") as f:
            file=base64.b64encode(f.read()).decode()
            data["base64String"] = file
        for i in range(2):
            res = requests.post(url,data, headers=self.headers)
            if res.status_code == 200:
                status = res.json()["status"]
                if status == 0:
                    print("上传任务结果成功")
                    return True
                elif status in (1002, 1003, 1004):
                    self.headers = {
                        "Content-Type": "application/json",
                        "token": self.apply_token()
                    }
                    continue
            else:
                print("上传任务结果失败")
                return False


