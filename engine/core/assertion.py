import re
import ast

from assertpy import assertpy
class Assert:

    def __init__(self, position, actual_result, expected_result):
        """
            position: 断言方法
            actual_result: 实际值 被断言的值
            expected_result: 预期值
        """
        self.comparator = position
        self.actual_result = actual_result
        self.expected_result = expected_result

    def compare(self):
        try:
            if self.comparator in ["equal", "equals", "相等", "字符相等"]:  # 等于
                assFailMsg = '断言 实际值({})与预期值({}) 字符相等，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_equal_to(self.expected_result)
            elif self.comparator in ["equalsList", "数组相等"]:  # 列表相同，包括列表顺序也相同
                assFailMsg = '断言 实际值({})与预期值({}) 数组相等，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(Assert.str2list(self.actual_result)).is_equal_to(Assert.str2list(self.expected_result))
            elif self.comparator in ["equalsDict", "对象相等"]:  # 字典相同
                assFailMsg = '断言 实际值({})与预期值({}) 对象相等，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(Assert.str2dict(self.actual_result)).is_equal_to(Assert.str2dict(self.expected_result))
            elif self.comparator in ["equalsNumber", "数字相等", "数值相等"]:  # 数字等于
                assFailMsg = '断言 实际值({})与预期值({}) 数值相等，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(Assert.str2num(self.actual_result)).is_equal_to(Assert.str2num(self.expected_result))
            elif self.comparator in ["equalIgnoreCase", "相等(忽略大小写)"]:  # 忽略大小写等于
                assFailMsg = '断言 实际值({})与预期值({}) 相等(忽略大小写)，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_equal_to_ignoring_case(self.expected_result)
            elif self.comparator in ["notEqual", "does not equal", "不等于"]:  # 不等于
                assFailMsg = '断言 实际值({}) 不等于 预期值({})，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_not_equal_to(self.expected_result)
            elif self.comparator in ["contains", "包含"]:  # 字符串包含该字符
                assFailMsg = '断言 实际值({}) 包含 预期值({})，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(Assert.to_str(self.actual_result)).contains((self.expected_result))
            elif self.comparator in ["notContains", "does no contains", "不包含"]:  # 字符串不包含该字符
                assFailMsg = '断言 实际值({}) 不包含 预期值({})，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).does_not_contain(*Assert.str2list(self.expected_result))
            elif self.comparator in ["containsOnly", "仅包含"]:  # 字符串仅包含该字符
                assFailMsg = '断言 实际值({}) 仅包含 预期值({})，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).contains_only(*Assert.str2list(self.expected_result))
            elif self.comparator in ["isNone", "none/null"]:  # 为none或null
                assFailMsg = '断言 实际值({}) 为none或null，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_none()
            elif self.comparator in ["notEmpty", "is not empty", "不为空"]:  # 不为空
                assFailMsg = '断言 实际值({}) 不为空，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_not_empty()
            elif self.comparator in ["empty", "is empty", "为空"]:  # 为空
                assFailMsg = '断言 实际值({}) 为空，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_empty()
            elif self.comparator in ["isTrue", "true"]:  # 是true
                assFailMsg = '断言 实际值({}) 是true，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_true()
            elif self.comparator in ["isFalse", "false"]:  # 是false
                assFailMsg = '断言 实际值({}) 是false，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_false()
            elif self.comparator in ["isStrType", "字符串"]:  # 是str的类型
                assFailMsg = '断言 实际值({}) 是字符串，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_type_of(str)
            elif self.comparator in ["isIntType", "整数"]:  # 是int的类型
                assFailMsg = '断言 实际值({}) 是整数，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_type_of(int)
            elif self.comparator in ["isFloatType", "浮点数"]:  # 是浮点的类型
                assFailMsg = '断言 实际值({}) 是浮点数，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_type_of(float)
            elif self.comparator in ["isInt", "is a number", "仅含数字"]:  # 字符串中仅含有数字
                assFailMsg = '断言 实际值({}) 仅含数字，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_digit()
            elif self.comparator in ["isLetter", "仅含字母"]:  # 字符串中仅含有字母
                assFailMsg = '断言 实际值({}) 仅含字母，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_alpha()
            elif self.comparator in ["isLower", "小写"]:  # 是小写的
                assFailMsg = '断言 实际值({}) 是小写的，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_lower()
            elif self.comparator in ["isUpper", "大写"]:  # 是大写的
                assFailMsg = '断言 实际值({}) 是大写的，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_upper()
            elif self.comparator in ["startWith", "开头是"]:  # 字符串以该字符开始
                assFailMsg = '断言 实际值({}) 开头是 预期值({})，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).starts_with(self.expected_result)
            elif self.comparator in ["endWith", "结尾是"]:  # 字符串以该字符结束
                assFailMsg = '断言 实际值({}) 结尾是 预期值({})，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).ends_with(self.expected_result)
            elif self.comparator in ["isIn", "has item", "包含对象", "被包含"]:  # 在这几个字符串中
                assFailMsg = '断言 实际值({}) 被包含在 预期值({}) 列表中，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_in(*Assert.str2list(self.expected_result))
            elif self.comparator in ["isNotIn", "不被包含"]:  # 不在这几个字符串中
                assFailMsg = '断言 实际值({}) 不被包含在 预期值({}) 列表中，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_not_in(*Assert.str2list(self.expected_result))
            elif self.comparator in ["isNotZero", "非0"]:  # 不是0
                assFailMsg = '断言 实际值({}) 不是0，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(Assert.str2num(self.actual_result)).is_not_zero()
            elif self.comparator in ["isZero", "为0"]:  # 是0
                assFailMsg = '断言 实际值({}) 是0，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(Assert.str2num(self.actual_result)).is_zero()
            elif self.comparator in ["isPositive", "正数"]:  # 是正数
                assFailMsg = '断言 实际值({}) 是正数，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_positive()
            elif self.comparator in ["isNegative", "负数"]:  # 是负数
                assFailMsg = '断言 实际值({}) 是负数，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_negative()
            elif self.comparator in ["isGreaterThan", " 大于"]:  # 大于
                assFailMsg = '断言 实际值({}) 大于 预期值({})，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_greater_than(Assert.str2num(self.expected_result))
            elif self.comparator in ["isGreaterThanOrEqualTo", "greater than or equal", ">=", " 大于等于"]:  # 大于等于
                assFailMsg = '断言 实际值({}) 大于等于 预期值({})，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_greater_than_or_equal_to(Assert.str2num(self.expected_result))
            elif self.comparator in ["isLessThan", " 小于"]:  # 小于
                assFailMsg = '断言 实际值({}) 小于 预期值({})，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_less_than(Assert.str2num(self.expected_result))
            elif self.comparator in ["isLessThanOrEqualTo", "less than or equal", "<=", " 小于等于"]:  # 小于等于
                assFailMsg = '断言 实际值({}) 小于等于 预期值({})，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_less_than_or_equal_to(Assert.str2num(self.expected_result))
            elif self.comparator in ["isBetween", " 在...之间"]:  # 在...之间
                assFailMsg = '断言 实际值({}) 在 预期值({}) 之间，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_between(*Assert.str2list(self.expected_result))
            elif self.comparator in ["isCloseTo", " 接近于"]:  # 接近于
                assFailMsg = '断言 实际值({}) 接近于 预期值({})，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(self.actual_result).is_close_to(*Assert.str2list(self.expected_result))
            elif self.comparator in ["listLenEqual","列表长度相等"]:  # 列表长度相等
                assFailMsg = '断言 实际值({}) 列表长度相等 预期值({})，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(Assert.list_len(self.actual_result)).is_equal_to(Assert.str2num(self.expected_result))
            elif self.comparator in ["listLenGreaterThan","列表长度大于"]:  # 列表长度大于
                assFailMsg = '断言 实际值({}) 列表长度大于 预期值({})，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(Assert.list_len(self.actual_result)).is_greater_than(Assert.str2num(self.expected_result))
            elif self.comparator in ["listLenLessThan","列表长度小于"]:  # 列表长度小于
                assFailMsg = '断言 实际值({}) 列表长度小于 预期值({})，断言失败：'.format(self.actual_result, self.expected_result)
                assertpy.assert_that(Assert.list_len(self.actual_result)).is_less_than_or_equal_to(Assert.str2num(self.expected_result))
            else:
                raise AssertionTypeNotExist('没有{}该断言类型'.format(self.comparator))
            return True, 'success'
        except AssertionError as e:
            ex = str(e).replace("Expected <", "Expected (").replace(">, ", "), ").replace(" <", " (").replace("> ", ") ")
            return False, assFailMsg + ex

    @staticmethod
    def str2num(value):
        if type(value) == int or type(value) == float:
            return value
        if value is None or len(value) == 0:
            return None
        elif re.fullmatch(r'-?[0-9]*\.?[0-9]*', value) is not None:
            if '.' in value:
                return float(value)
            else:
                return int(value)
        else:
            return value

    @staticmethod
    def str2list(value):  # 适配期望值为多个值（输入字符串转化成列表）
        if type(value) == list or type(value) == int or type(value) == float:
            return value
        if value is None or len(value) == 0:
            return None
        value_list = []
        if value.startswith('[') and value.endswith(']'):
            for item in value[1:-1].split(','):
                item_strip = item.strip()
                if re.fullmatch(r'-?[0-9]*\.?[0-9]*', item_strip) is not None:  # 匹配为数字
                    if '.' in item_strip:
                        value_list.append(float(item_strip))
                    else:
                        value_list.append(int(item_strip))
                else:  # 字符串
                    value_list.append(item_strip[1:-1])
            return value_list
        else:
            return value

    @staticmethod
    def str2dict(value):  # 输入字符串转化成字典
        if type(value) == dict or type(value) == int or type(value) == float:
            return value
        if value is None or len(value) == 0:
            return None
        value_dict={}
        if value.startswith('{') and value.endswith('}'):
            # for item in value[1:-1].split(','):
            #     value_dict = ast.literal_eval(value)
            return ast.literal_eval(value)
        else:
            return value

    @staticmethod
    def to_str(value):  # 输入转化成字符串
        if type(value) == int or type(value) == float:
            return value
        if value is None or len(value) == 0:
            return ""
        if type(value) == str:
            return value
        else:
            return str(value)

    @staticmethod
    def list_len(value):  # 返回输入列表的长度
        value2list=Assert.str2list(value)
        if type(value2list) != list:
            raise AssertionTypeNotExist('传入实际值({}) 不是列表格式'.format(value))
        else:
            return len(value2list)


class AssertionTypeNotExist(Exception):
    """断言类型错误"""