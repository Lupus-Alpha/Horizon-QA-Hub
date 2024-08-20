from selenium.common.exceptions import NoSuchElementException

from core.assertion import Assert
from core.web.driver.__init__ import Operation


class Assertion(Operation):
    """断言类操作"""

    def assert_page_title(self, assertion, expect):
        """断言页面标题"""
        try:
            actual = self.driver.title
            self.test.debugLog("成功获取title:%s" % str(actual))
        except Exception as e:
            self.test.errorLog("无法获取title")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_page_url(self, assertion, expect):
        """断言页面url"""
        try:
            actual = self.driver.current_url
            self.test.debugLog("成功获取url:%s" % str(actual))
        except Exception as e:
            self.test.errorLog("无法获取url")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_page_source(self, assertion, expect):
        """断言页面源码"""
        try:
            actual = self.driver.page_source
            self.test.debugLog("成功获取page source:%s" % str(actual))
        except Exception as e:
            self.test.errorLog("无法获取page source")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_ele_text(self, element, assertion, expect):
        """断言元素文本"""
        try:
            actual = self.find_element(element).text
            self.test.debugLog("成功获取元素text:%s" % str(actual))
        except NoSuchElementException as e:
            raise e
        except Exception as e:
            self.test.errorLog("无法获取元素text")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_ele_tag(self, element, assertion, expect):
        """断言元素tag"""
        try:
            actual = self.find_element(element).tag_name
            self.test.debugLog("成功获取元素tag name:%s" % str(actual))
        except NoSuchElementException as e:
            raise e
        except Exception as e:
            self.test.errorLog("无法获取元素tag name")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_ele_size(self, element, assertion, expect):
        """断言元素尺寸"""
        try:
            actual = self.find_element(element).size
            self.test.debugLog("成功获取元素size:%s" % str(actual))
        except NoSuchElementException as e:
            raise e
        except Exception as e:
            self.test.errorLog("无法获取元素size")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_ele_height(self, element, assertion, expect):
        """断言元素高度"""
        try:
            actual = self.find_element(element).size.get("height")
            self.test.debugLog("成功获取元素height:%s" % str(actual))
        except NoSuchElementException as e:
            raise e
        except Exception as e:
            self.test.errorLog("无法获取元素height")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_ele_width(self, element, assertion, expect):
        """断言元素宽度"""
        try:
            actual = self.find_element(element).size.get("width")
            self.test.debugLog("成功获取元素width:%s" % str(actual))
        except NoSuchElementException as e:
            raise e
        except Exception as e:
            self.test.errorLog("无法获取元素width")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_ele_location(self, element, assertion, expect):
        """断言元素位置"""
        try:
            actual = self.find_element(element).location
            self.test.debugLog("成功获取元素location:%s" % str(actual))
        except NoSuchElementException as e:
            raise e
        except Exception as e:
            self.test.errorLog("无法获取元素location")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_ele_x(self, element, assertion, expect):
        """断言元素X坐标"""
        try:
            actual = self.find_element(element).location.get("x")
            self.test.debugLog("成功获取元素location x:%s" % str(actual))
        except NoSuchElementException as e:
            raise e
        except Exception as e:
            self.test.errorLog("无法获取元素location x")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_ele_y(self, element, assertion, expect):
        """断言元素Y坐标"""
        try:
            actual = self.find_element(element).location.get("y")
            self.test.debugLog("成功获取元素location y:%s" % str(actual))
        except NoSuchElementException as e:
            raise e
        except Exception as e:
            self.test.errorLog("无法获取元素location y")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_ele_attribute(self, element, name, assertion, expect):
        """断言元素属性"""
        try:
            actual = self.find_element(element).get_attribute(name)
            self.test.debugLog("成功获取元素attribute:%s" % str(actual))
        except NoSuchElementException as e:
            raise e
        except Exception as e:
            self.test.errorLog("无法获取元素attribute")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_ele_selected(self, element, assertion, expect):
        """断言元素是否选中"""
        try:
            actual = self.find_element(element).is_selected()
            self.test.debugLog("成功获取元素selected:%s" % str(actual))
        except NoSuchElementException as e:
            raise e
        except Exception as e:
            self.test.errorLog("无法获取元素selected")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_ele_enabled(self, element, assertion, expect):
        """断言元素是否启用"""
        try:
            actual = self.find_element(element).is_enabled()
            self.test.debugLog("成功获取元素enabled:%s" % str(actual))
        except NoSuchElementException as e:
            raise e
        except Exception as e:
            self.test.errorLog("无法获取元素enabled")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_ele_displayed(self, element, assertion, expect):
        """断言元素是否显示"""
        try:
            actual = self.find_element(element).is_displayed()
            self.test.debugLog("成功获取元素displayed:%s" % str(actual))
        except NoSuchElementException as e:
            raise e
        except Exception as e:
            self.test.errorLog("无法获取元素displayed")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_ele_css(self, element, name, assertion, expect):
        """断言元素css样式"""
        try:
            actual = self.find_element(element).value_of_css_property(name)
            self.test.debugLog("成功获取元素css %s:%s" % (name, str(actual)))
        except NoSuchElementException as e:
            raise e
        except Exception as e:
            self.test.errorLog("无法获取元素css %s" % name)
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_ele_existed(self, element, assertion, expect):
        """断言元素是否存在"""
        try:
            try:
                self.find_elements(element)
                actual = True
            except NoSuchElementException:
                actual = False
            self.test.debugLog("成功获取元素existed:%s" % str(actual))
        except Exception as e:
            self.test.errorLog("无法获取元素existed")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_window_position(self, assertion, expect):
        """断言窗口位置"""
        try:
            actual = self.driver.get_window_position()
            self.test.debugLog("成功获取窗口position:%s" % str(actual))
        except Exception as e:
            self.test.errorLog("无法获取窗口position")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_window_x(self, assertion, expect):
        """断言窗口X坐标"""
        try:
            actual = self.driver.get_window_position().get("x")
            self.test.debugLog("成功获取窗口position x:%s" % str(actual))
        except Exception as e:
            self.test.errorLog("无法获取窗口position x")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_window_y(self, assertion, expect):
        """断言窗口Y坐标"""
        try:
            actual = self.driver.get_window_position().get("y")
            self.test.debugLog("成功获取窗口position y:%s" % str(actual))
        except Exception as e:
            self.test.errorLog("无法获取窗口position y")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_window_size(self, assertion, expect):
        """断言窗口大小"""
        try:
            actual = self.driver.get_window_size()
            self.test.debugLog("成功获取窗口size:%s" % str(actual))
        except Exception as e:
            self.test.errorLog("无法获取窗口size")
            raise e
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_window_width(self, assertion, expect):
        """断言窗口宽度"""
        try:
            actual = self.driver.get_window_size().get("width")
            self.test.debugLog("成功获取窗口width:%s" % str(actual))
        except Exception as e:
            self.test.errorLog("无法获取窗口width")
            raise e        
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_window_height(self, assertion, expect):
        """断言窗口高度"""
        try:
            actual = self.driver.get_window_size().get("height")
            self.test.debugLog("成功获取窗口height:%s" % str(actual))
        except Exception as e:
            self.test.errorLog("无法获取窗口height")
            raise e        
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_cookies(self, assertion, expect):
        """断言cookies"""
        try:
            actual = self.driver.get_cookies()
            self.test.debugLog("成功获取cookies:%s" % str(actual))
        except Exception as e:
            self.test.errorLog("无法获取cookies")
            raise e        
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def assert_cookie(self, name, assertion, expect):
        """断言cookie"""
        try:
            actual = self.driver.get_cookie(name)
            self.test.debugLog("成功获取cookie %s:%s" % (name, str(actual)))
        except Exception as e:
            self.test.errorLog("无法获取cookie %s" % name)
            raise e        
        else:
            result, msg = Assert(assertion, actual, expect).compare()
            return result, msg

    def custom(self, **kwargs):
        """自定义"""
        code = kwargs["code"]
        names = locals()
        names["element"] = kwargs["element"]
        names["data"] = kwargs["data"]
        names["driver"] = self.driver
        names["test"] = self.test
        try:
            """断言操作需要返回被断言的值 以sys_return(value)返回"""
            def sys_return(res):
                names["_exec_result"] = res
            exec(code)
            self.test.debugLog("成功执行 %s" % kwargs["trans"])
        except NoSuchElementException as e:
            raise e
        except Exception as e:
            self.test.errorLog("无法执行 %s" % kwargs["trans"])
            raise e
        else:
            result, msg = Assert(kwargs["data"]["assertion"], names["_exec_result"], kwargs["data"]["expect"]).compare()
            return result, msg

