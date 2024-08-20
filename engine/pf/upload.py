import os
from concurrent.futures import ThreadPoolExecutor, as_completed
from pf.api import Api

class Upload(object):
    def __init__(self,files):
        self.files = files

    def upload(self,image_path,uuid):
        res = Api().upload_image(image_path, uuid)
        return res



    def upload_files(self,image_path):
        all_task = []
        with ThreadPoolExecutor(max_workers=2) as pool:
            for file in self.files:
                if file.endswith(".png"):
                    uuid = file[:-4]
                    all_task.append(pool.submit(self.upload, image_path, uuid))
                else:
                    os.remove(os.path.join(image_path,file))
        for result in as_completed(all_task):
            print(result)





