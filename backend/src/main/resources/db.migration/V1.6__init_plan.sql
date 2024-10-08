CREATE TABLE `collection` (
    `id`       VARCHAR(50) NOT NULL COMMENT '集合id',
	`name`	   VARCHAR(100) NOT NULL COMMENT '集合名称',
	`device_id`  VARCHAR(50) DEFAULT NULL COMMENT '执行设备',
    `description` varchar(200) NOT NULL COMMENT '集合描述',
	`project_id`   varchar(50) NOT NULL COMMENT '所属项目id',
    `update_user`    varchar(50) NOT NULL COMMENT '更新人',
	`create_user`    varchar(50) NOT NULL COMMENT '创建人',
    `create_time` bigint(13)  NOT NULL COMMENT '创建时间',
	`update_time` bigint(13)  NOT NULL COMMENT '更新时间',
	`status` int(1)  DEFAULT 1 COMMENT '状态0删除 1正常',
    PRIMARY KEY (`id`), INDEX(`project_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `collection_case` (
    `id`       VARCHAR(50) NOT NULL COMMENT '集合用例id',
    `index`    int(8) NOT NULL COMMENT '用例序号',
	`collection_id`	   VARCHAR(50) NOT NULL COMMENT '集合id',
    `case_id`  VARCHAR(50) NOT NULL COMMENT '用例id',
    PRIMARY KEY (`id`), INDEX(`collection_id`), INDEX(`case_id`), INDEX(`index`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `plan` (
    `id`       VARCHAR(50) NOT NULL COMMENT '计划id',
	`name`	   VARCHAR(100) NOT NULL COMMENT '计划名称',
	`description`	   VARCHAR(200) NOT NULL COMMENT '计划描述',
    `environment_id`  VARCHAR(50) DEFAULT NULL COMMENT '执行环境',
    `retry`  VARCHAR(1) NOT NULL COMMENT '是否失败重试',
    `engine_id`  VARCHAR(50) NOT NULL COMMENT '执行引擎',
	`project_id`   varchar(50) NOT NULL COMMENT '所属项目id',
    `update_user`    varchar(50) NOT NULL COMMENT '更新人',
    `create_user`    varchar(50) NOT NULL COMMENT '创建人',
    `create_time` bigint(13)  NOT NULL COMMENT '创建时间',
	`update_time` bigint(13)  NOT NULL COMMENT '更新时间',
	`status` int(1)  DEFAULT 1 COMMENT '状态0删除 1正常',
    PRIMARY KEY (`id`), INDEX(`project_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `plan_collection` (
    `id`       VARCHAR(50) NOT NULL COMMENT '计划集合id',
	`plan_id`	   VARCHAR(50) NOT NULL COMMENT '计划id',
    `collection_id` varchar(50) NOT NULL COMMENT '集合id',
    PRIMARY KEY (`id`), INDEX(`plan_id`), INDEX(`collection_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;
