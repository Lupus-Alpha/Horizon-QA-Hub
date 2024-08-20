import os

BASE_PATH = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DATA_PATH = os.path.join(BASE_PATH, "data")
LOG_PATH = os.path.join(BASE_PATH, "log")
plate_form_url = "http://localhost:8089/openapi"
engine_code = "e59cfb9cf01d11eea1172810d911c6ad"
engine_secret = "fa7b7ed30e6911eebe9a145afc2ab954"