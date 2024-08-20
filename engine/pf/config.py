import configparser
import os
BASE_PATH = os.path.dirname(os.path.dirname(os.path.abspath(__file__))) #获取当前文件所在的目录的绝对路径
CONFIG_PATH = os.path.join(BASE_PATH, "config", "config.ini")
BROWSER_PATH = os.path.join(BASE_PATH, "browser")
class IniReader:
    """
    读取ini文件中的内容,返回值：str。
        指定config_name，param_name读取对应的值
    """

    def __init__(self, config_ini=CONFIG_PATH):
        if os.path.exists(config_ini):
            self.ini_file = config_ini
        else:
            raise FileNotFoundError('文件不存在！')

    def data(self, section, option):
        config = configparser.ConfigParser()    # 初始化配置读取方法
        config.read(self.ini_file, encoding="utf-8")    # 读取配置
        value = config.get(section, option) # 根据配置所属的类和配置名称读取配置
        return value

    def option(self, section):
        config = configparser.ConfigParser()
        config.read(self.ini_file, encoding="utf-8")
        options = config.options(section)   # 获取配置类下的所有配置
        option = {}
        for key in options:
            option[key] = self.data(section, key)
        return option

    def modify(self, section, option, value):
        config = configparser.ConfigParser()
        config.read(self.ini_file, encoding="utf-8")
        config.set(section, option, value)   # 修改配置类
        config.write(open(self.ini_file, "r+", encoding="utf-8"))   # 回写



class Config(object):
    """"配置文件"""
    def __init__(self, path=CONFIG_PATH):
        reader = IniReader(path)
        env_dist = os.environ   # 获取环境变量
        self.url = env_dist.get("PLATFORM_URL")
        if self.url is None:
            self.url = reader.data("Platform", "url")
        self.enable_proxy = reader.data("Platform", "enable-proxy")
        self.enable_stderr = reader.data("Platform", "enable-stderr")
        self.engine = env_dist.get("ENGINE_CODE")
        if self.engine is None:
            self.engine = reader.data("Engine", "engine-code")
        self.secret = env_dist.get("ENGINE_SECRET")
        if self.secret is None:
            self.secret = reader.data("Engine", "engine-secret")
        self.header = reader.option("Header")
        self.platform_proxy = reader.option("PlatformProxy")
        self.browser_opt = reader.data("WebDriver", "options")
        self.browser_path = os.path.join(BROWSER_PATH, reader.data("WebDriver", "path"))
