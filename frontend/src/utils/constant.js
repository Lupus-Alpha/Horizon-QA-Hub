/**
 * app测试元素定位方式
 **/
 export const locateBys = {
    android: [
        { label: "Xpath定位", value: "XPATH" },
        { label: "属性定位", value: "PROP" }
    ],
    apple: [
        { label: "Xpath定位", value: "XPATH" },
        { label: "属性定位", value: "PROP" },
        { label: "Predicate定位", value: "PRED" },
        { label: "ClassChain定位", value: "CLASS" }
    ]
}

/**
 * app测试定位属性值
 **/
export const locateProps = {
    android: ["text","textContains","textMatches","textStartsWith","className","classNameMatches","description","descriptionContains","descriptionMatches","descriptionStartsWith","checkable","checked","clickable","longClickable","scrollable","enabled","focusable","focused","selected","packageName","packageNameMatches","resourceId","resourceIdMatches"],
    apple: ["id","className", "name", "value", "label"]
}

/**
 * 断言方法
 **/
 export const assertions = [
    {
      "id": "isFalse",
      "name": "false"
    },
    {
      "id": "isNone",
      "name": "none/null"
    },
    {
      "id": "isTrue",
      "name": "true"
    },
    {
      "id": "notEmpty",
      "name": "不为空"
    },
    {
      "id": "notContains",
      "name": "不包含"
    },
    {
      "id": "notEqual",
      "name": "不相等"
    },
    {
      "id": "isNotIn",
      "name": "不被包含"
    },
    {
      "id": "isZero",
      "name": "为0"
    },
    {
      "id": "empty",
      "name": "为空"
    },
    {
      "id": "containsOnly",
      "name": "仅包含"
    },
    {
      "id": "isLetter",
      "name": "仅含字母"
    },
    {
      "id": "isInt",
      "name": "仅含数字"
    },
    {
      "id": "listLenGreaterThan",
      "name": "列表长度大于"
    },
    {
      "id": "listLenLessThan",
      "name": "列表长度小于"
    },
    {
      "id": "listLenEqual",
      "name": "列表长度相等"
    },
    {
      "id": "contains",
      "name": "包含"
    },
    {
      "id": "isBetween",
      "name": "在...之间"
    },
    {
      "id": "isGreaterThan",
      "name": "大于"
    },
    {
      "id": "isGreaterThanOrEqualTo",
      "name": "大于等于"
    },
    {
      "id": "isUpper",
      "name": "大写"
    },
    {
      "id": "isStrType",
      "name": "字符串"
    },
    {
      "id": "equalsDict",
      "name": "对象相等"
    },
    {
      "id": "isLessThan",
      "name": "小于"
    },
    {
      "id": "isLessThanOrEqualTo",
      "name": "小于等于"
    },
    {
      "id": "isLower",
      "name": "小写"
    },
    {
      "id": "startWith",
      "name": "开头是"
    },
    {
      "id": "isCloseTo",
      "name": "接近于"
    },
    {
      "id": "equalsNumber",
      "name": "数字相等"
    },
    {
      "id": "equalsList",
      "name": "数组相等"
    },
    {
      "id": "isIntType",
      "name": "整数"
    },
    {
      "id": "isPositive",
      "name": "正数"
    },
    {
      "id": "isFloatType",
      "name": "浮点数"
    },
    {
      "id": "equals",
      "name": "相等"
    },
    {
      "id": "equalIgnoreCase",
      "name": "相等(忽略大小写)"
    },
    {
      "id": "endWith",
      "name": "结尾是"
    },
    {
      "id": "isIn",
      "name": "被包含"
    },
    {
      "id": "isNegative",
      "name": "负数"
    },
    {
      "id": "isNotZero",
      "name": "非0"
    }
]