INSERT INTO `function`
(`id`, `name`, `from`, `params`, `code`, `description`, `project_id`, `update_user`, `create_user`, `create_time`, `update_time`)
VALUES
(UUID(), 'bothify', 'system', '[{\"type\": \"String\", \"paramName\": \"text\", \"description\": \"生成表达式 例 \'##??\'\"},{\"type\": \"String\", \"paramName\": \"letters\", \"description\": \"生成字母取值范围 默认26个字母\"}]', '', '随机生成数字字母组合字符串', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'lexify', 'system', '[{\"type\": \"String\", \"paramName\": \"text\", \"description\": \"生成表达式 例 \'????\'\"},{\"type\": \"String\", \"paramName\": \"letters\", \"description\": \"生成字母取值范围 默认26个字母\"}]', '', '随机生成纯字母字符串', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'numerify','system', '[{\"type\": \"String\", \"paramName\": \"text\", \"description\": \"生成表达式 例 \'####\'\"}]', '', '随机生成纯数字字符串', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'random_int','system', '[{\"type\": \"Int\", \"paramName\": \"min\", \"description\": \"最小值\"}, {\"type\": \"Int\", \"paramName\": \"max\", \"description\": \"最大值\"}, {\"type\": \"Int\", \"paramName\": \"step\", \"description\": \"间隔\"}]', '', '随机生成指定区间数字', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'pyfloat', 'system', '[{\"type\": \"Int\", \"paramName\": \"left_digits\", \"description\": \"小数点左边整数位数\"}, {\"type\": \"Int\", \"paramName\": \"right_digits\", \"description\": \"小数点右边小数位数\"}, {\"type\": \"Boolean\", \"paramName\": \"positive\", \"description\": \"是否只有正数 默认True\"}]', '', '随机生成指定长度的小数', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'random_number', 'system', '[{\"type\": \"Int\", \"paramName\": \"digits\", \"description\": \"数值长度\"}, {\"type\": \"Boolean\", \"paramName\": \"fix_len\", \"description\": \"是否必须该长度 False时可以小于该长度\"}]', '', '随机生成指定长度数字', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'country_code', 'system', '[]', '', '随机生成国家编码', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'ean', 'system', '[{\"type\": \"Int\", \"paramName\": \"length\", \"description\": \"条码长度 8或13\"}]', '', '随机生成国际商品条编码', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'localized_ean', 'system', '[{\"type\": \"Int\", \"paramName\": \"length\", \"description\": \"条码长度 8或13\"}]', '', '随机生成当地商品条编码', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'credit_card_full','system', '[]', '', '随机生成信用卡完整信息', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'credit_card_number', 'system', '[]', '', '随机生成信用卡号', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'credit_card_provider', 'system', '[]', '', '随机生成信用卡类型', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'credit_card_security_code', 'system', '[]', '', '随机生成信用卡安全码', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'date', 'system', '[{\"type\": \"String\", \"paramName\": \"s\", \"description\": \"时间表达式 例 %Y-%m-%d\"}]', '', '随机生成日期', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'time', 'system', '[{\"type\": \"String\", \"paramName\": \"s\", \"description\": \"时间表达式 例 %H:%M:%S\"}]', '', '随机生成时间', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'file_extension', 'system', '[]', '', '随机生成文件拓展名', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'file_name', 'system', '[]', '', '随机生成文件名', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'file_path', 'system', '[]', '', '随机生成文件路径', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'mime_type', 'system', '[]', '', '随机生成mime type', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'unix_device', 'system', '[]', '', '随机生成unix设备', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'unix_partition', 'system', '[]', '', '随机生成unix分区', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'domain_name', 'system', '[]', '', '随机生成域名地址', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'email', 'system', '[]', '', '随机生成邮箱地址', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'hostname', 'system', '[]', '', '随机生成主机地址', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'image_url', 'system', '[]', '', '随机生成图片地址', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'ipv4', 'system', '[]', '', '随机生成ipv4地址', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'ipv4_private', 'system', '[]', '', '随机生成局域网ipv4地址', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'ipv4_public', 'system', '[]', '', '随机生成公网ipv4地址', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'ipv6', 'system', '[]', '', '随机生成ipv6地址', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'uri_path','system', '[]', '', '随机生成网址文件路径', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'isbn10', 'system', '[]', '', '随机生成10位ISBN', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'isbn13', 'system', '[]', '', '随机生成13位ISBN', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'paragraph', 'system', '[]', '', '随机生成一个段落', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'paragraphs', 'system', '[]', '', '随机生成多个段落', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'sentence', 'system', '[]', '', '随机生成一句话', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'sentences', 'system', '[]', '', '随机生成多句话', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'text', 'system', '[]', '', '随机生成一篇文章', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'texts', 'system', '[]', '', '随机生成多篇文章', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'word', 'system', '[]', '', '随机生成一个词语', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'words', 'system', '[]', '', '随机生成多个词语', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'password', 'system', '[{\"type\": \"Int\", \"paramName\": \"length\", \"description\": \"密码长度\"}]', '', '随机生成指定长度密码', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),

(UUID(), 'loadfile', 'system', '[{\"type\": \"String\", \"paramName\": \"uuid\", \"description\": \"文件uuid\"}]', '', '下载并读取文件', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'savefile', 'system', '[{\"type\": \"String\", \"paramName\": \"uuid\", \"description\": \"文件uuid\"}]', '', '下载并保存文件 返回文件路径', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'b64encode_str', 'system', '[{\"type\": \"String\", \"paramName\": \"s\", \"description\": \"待编码字符串\"}]', '', '对字符串base64编码', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'b64encode_bytes', 'system', '[{\"type\": \"Bytes\", \"paramName\": \"s\", \"description\": \"待编码字节流\"}]', '', '对字节流base64编码', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'b64encode_file', 'system', '[{\"type\": \"String\", \"paramName\": \"uuid\", \"description\": \"文件uuid\"}]', '', '下载读取文件并base64编码', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'b64decode_toStr', 'system', '[{\"type\": \"String\", \"paramName\": \"s\", \"description\": \"待解码字符串\"}]', '', '对base64解码成字符串', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'b64decode_toBytes', 'system', '[{\"type\": \"String\", \"paramName\": \"s\", \"description\": \"待解码字符串\"}]', '', '对base64解码成字节流', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'arithmetic', 'system', '[{\"type\": \"String\", \"paramName\": \"expression\", \"description\": \"运算表达式\"}]', '', '四则运算', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'current_time', 'system', '[{\"type\": \"String\", \"paramName\": \"s\", \"description\": \"时间表达式 传None则返回时间戳\"}]', '', '获取当前时间', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'year_shift', 'system', '[{\"type\": \"Float\", \"paramName\": \"shift\", \"description\": \"时间偏移量 可为负数\"}, {\"type\": \"String\", \"paramName\": \"s\", \"description\": \"时间表达式 传None则返回时间戳\"}]', '', '获取当前时间前后年份时间', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'month_shift', 'system', '[{\"type\": \"Float\", \"paramName\": \"shift\", \"description\": \"时间偏移量 可为负数\"}, {\"type\": \"String\", \"paramName\": \"s\", \"description\": \"时间表达式 传None则返回时间戳\"}]', '', '获取当前时间前后月份时间', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'week_shift', 'system', '[{\"type\": \"Float\", \"paramName\": \"shift\", \"description\": \"时间偏移量 可为负数\"}, {\"type\": \"String\", \"paramName\": \"s\", \"description\": \"时间表达式 传None则返回时间戳\"}]', '', '获取当前时间前后天数时间', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'date_shift', 'system', '[{\"type\": \"Float\", \"paramName\": \"shift\", \"description\": \"时间偏移量 可为负数\"}, {\"type\": \"String\", \"paramName\": \"s\", \"description\": \"时间表达式 传None则返回时间戳\"}]', '', '获取当前时间前后周数时间', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'hour_shift', 'system', '[{\"type\": \"Float\", \"paramName\": \"shift\", \"description\": \"时间偏移量 可为负数\"}, {\"type\": \"String\", \"paramName\": \"s\", \"description\": \"时间表达式 传None则返回时间戳\"}]', '', '获取当前时间前后小时时间', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'minute_shift', 'system', '[{\"type\": \"Float\", \"paramName\": \"shift\", \"description\": \"时间偏移量 可为负数\"}, {\"type\": \"String\", \"paramName\": \"s\", \"description\": \"时间表达式 传None则返回时间戳\"}]', '', '获取当前时间前后分钟时间', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'second_shift', 'system', '[{\"type\": \"Float\", \"paramName\": \"shift\", \"description\": \"时间偏移量 可为负数\"}, {\"type\": \"String\", \"paramName\": \"s\", \"description\": \"时间表达式 传None则返回时间戳\"}]', '', '获取当前时间前后秒钟时间', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'lenof', 'system', '[{\"type\": \"JSONArray\", \"paramName\": \"array\", \"description\": \"被取值数组\"}]', '', '获取数组长度', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'indexof', 'system', '[{\"type\": \"JSONArray\", \"paramName\": \"array\", \"description\": \"被取值数组\"}, {\"type\": \"Int\", \"paramName\": \"index\", \"description\": \"取值序号\"}]', '', '根据序号获取数组元素', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'keyof', 'system', '[{\"type\": \"JSONObject\", \"paramName\": \"map\", \"description\": \"被取值字典\"}, {\"type\": \"String\", \"paramName\": \"key\", \"description\": \"取值键值\"}]', '', '根据键值获取字典元素值', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'pinyin',  'system', '[{\"type\": \"String\", \"paramName\": \"cname\", \"description\": \"中文名\"}]', '', '获取中文的拼音', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'substing', 'system', '[{\"type\": \"String\", \"paramName\": \"s\", \"description\": \"被截取字符串\"}, {\"type\": \"Int\", \"paramName\": \"start\", \"description\": \"截取开始位数 可为负数\"}, {\"type\": \"Int\", \"paramName\": \"end\", \"description\": \"截取结束位数 可为负数\"}]', '', '截取字符串', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'extract',  'system', '[{\"type\": \"Other\", \"paramName\": \"data\", \"description\": \"提取后的值\"}]', '', '获取json提取值 通常与#{jsonpath}并用', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'replace', 'system', '[{\"type\": \"String\", \"paramName\": \"s\", \"description\": \"被替换字符串\"}, {\"type\": \"String\", \"paramName\": \"old\", \"description\": \"替换目标值\"}, {\"type\": \"String\", \"paramName\": \"new\", \"description\": \"替换后的值\"}]', '', '替换字符串', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'map_dumps',  'system', '[{\"type\": \"JSONObject\", \"paramName\": \"tar\", \"description\": \"序列化字典\"}]', '', '对字典进行序列化', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.','')),
(UUID(), 'array_dumps',  'system', '[{\"type\": \"JSONArray\", \"paramName\": \"tar\", \"description\": \"序列化数组\"}]', '', '对数组进行序列化', 'system', 'system_admin_user', 'system_admin_user', REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.',''));


