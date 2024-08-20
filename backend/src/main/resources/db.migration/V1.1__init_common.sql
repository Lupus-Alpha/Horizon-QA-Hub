CREATE TABLE `test_file` (
    `id`       VARCHAR(50) NOT NULL COMMENT '测试文件id',
	`name`	   VARCHAR(100) NOT NULL COMMENT '文件名称',
    `file_path` varchar(500) NOT NULL COMMENT '文件路径',
    `description` varchar(200) DEFAULT NULL COMMENT '文件描述',
    `project_id` varchar(50) NOT NULL COMMENT '所属项目id',
	`update_user`    varchar(50) NOT NULL COMMENT '更新人',
    `create_user`    varchar(50) NOT NULL COMMENT '创建人',
    `create_time` bigint(13)  NOT NULL COMMENT '创建时间',
	`update_time` bigint(13)  NOT NULL COMMENT '更新时间',
	`status`    int(1) DEFAULT 1 COMMENT '状态 0: 删除, 1: 正常',
    PRIMARY KEY (`id`), INDEX(`project_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `common_param` (
    `id`       VARCHAR(50) NOT NULL COMMENT '参数id',
	`name`	   VARCHAR(50) NOT NULL COMMENT '参数名',
    `param_data`  text NOT NULL COMMENT '参数值',
    `group_name`  VARCHAR(20) NOT NULL COMMENT '参数组名称',
    `data_type` varchar(20) NOT NULL COMMENT '数据类型',
    `description` varchar(200) DEFAULT NULL COMMENT '参数描述',
    `project_id` varchar(50) NOT NULL COMMENT '所属项目id',
	`update_user`    varchar(50) NOT NULL COMMENT '更新人',
    `create_user`    varchar(50) NOT NULL COMMENT '创建人',
    `create_time` bigint(13)  NOT NULL COMMENT '创建时间',
	`update_time` bigint(13)  NOT NULL COMMENT '更新时间',
	`status`    int(1) DEFAULT 1 COMMENT '状态 0: 删除, 1: 正常',
    PRIMARY KEY (`id`), INDEX(`project_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `function` (
    `id`       VARCHAR(50) NOT NULL COMMENT '函数id',
	`name`	   VARCHAR(50) NOT NULL COMMENT '函数名',
    `from` varchar(20) NOT NULL COMMENT '函数来源',
    `params` json DEFAULT NULL COMMENT '入参定义',
    `code`  text DEFAULT NULL COMMENT '函数代码',
    `description` varchar(200) DEFAULT NULL COMMENT '函数描述',
    `project_id` varchar(50) NOT NULL COMMENT '所属项目id',
	`update_user`    varchar(50) NOT NULL COMMENT '更新人',
    `create_user`    varchar(50) NOT NULL COMMENT '创建人',
    `create_time` bigint(13)  NOT NULL COMMENT '创建时间',
	`update_time` bigint(13)  NOT NULL COMMENT '更新时间',
	`status`    int(1) DEFAULT 1 COMMENT '状态 0: 删除, 1: 正常',
    PRIMARY KEY (`id`), INDEX(`project_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `operation` (
    `id`       VARCHAR(50) NOT NULL COMMENT '操作id',
	`name`	   VARCHAR(50) NOT NULL COMMENT '操作名',
	`type`	   VARCHAR(20) NOT NULL COMMENT '操作分类',
    `from`     VARCHAR(20) NOT NULL COMMENT '操作来源',
    `system`     VARCHAR(20) NOT NULL COMMENT '适用系统: web,common,android,apple',
    `elements` json DEFAULT NULL COMMENT '元素定义',
    `data` json DEFAULT NULL COMMENT '数据定义',
    `code`  text DEFAULT NULL COMMENT '操作代码',
    `description` varchar(200) DEFAULT NULL COMMENT '操作描述',
    `project_id` varchar(50) NOT NULL COMMENT '所属项目id',
	`update_user`    varchar(50) NOT NULL COMMENT '更新人',
    `create_user`    varchar(50) NOT NULL COMMENT '创建人',
    `create_time` bigint(13)  NOT NULL COMMENT '创建时间',
	`update_time` bigint(13)  NOT NULL COMMENT '更新时间',
	`status`    int(1) DEFAULT 1 COMMENT '状态 0: 删除, 1: 正常',
    PRIMARY KEY (`id`), INDEX(`project_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;




