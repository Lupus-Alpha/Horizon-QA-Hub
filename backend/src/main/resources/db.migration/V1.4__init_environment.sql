CREATE TABLE `environment` (
    `id`       VARCHAR(50) NOT NULL COMMENT '环境id',
	`name`	   VARCHAR(50) NOT NULL COMMENT '环境名称',
    `description` varchar(200) DEFAULT NULL COMMENT '环境描述',
	`project_id`   varchar(50) NOT NULL COMMENT '所属项目id',
	`update_user`    varchar(50) NOT NULL COMMENT '更新人',
	`create_user`    varchar(50) NOT NULL COMMENT '创建人',
    `create_time` bigint(13)  NOT NULL COMMENT '创建时间',
	`update_time` bigint(13)  NOT NULL COMMENT '更新时间',
	`status` int(1)  DEFAULT 1 COMMENT '状态 0删除 1正常',
    PRIMARY KEY (`id`),  INDEX(`project_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `server_sign` (
    `id`       VARCHAR(50) NOT NULL COMMENT '服务标识id',
	`name`	   VARCHAR(20) NOT NULL COMMENT '服务标识名称',
    `description`  VARCHAR(200) DEFAULT NULL COMMENT '服务标识描述',
    `project_id`  VARCHAR(50) NOT NULL COMMENT '所属项目id',
    `create_time` bigint(13)  NOT NULL COMMENT '创建时间',
	`update_time` bigint(13)  NOT NULL COMMENT '更新时间',
	`status` int(1)  DEFAULT 1 COMMENT '状态 0删除 1正常',
    PRIMARY KEY (`id`), INDEX(`project_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `domain` (
    `id`       VARCHAR(50) NOT NULL COMMENT '域名id',
    `type`	   VARCHAR(50) NOT NULL COMMENT '域名标识类型 sign服务标识 path路由标识',
	`domain_key`	   VARCHAR(100) NOT NULL COMMENT '域名标识',
    `domain_value`  VARCHAR(200) NOT NULL COMMENT '域名值',
    `environment_id`  VARCHAR(50) NOT NULL COMMENT '所属环境id',
	`update_user`    varchar(50) NOT NULL COMMENT '更新人',
    `create_user`    varchar(50) NOT NULL COMMENT '创建人',
    `create_time` bigint(13)  NOT NULL COMMENT '创建时间',
	`update_time` bigint(13)  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`), INDEX(`environment_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `engine` (
    `id`       VARCHAR(50) NOT NULL COMMENT '引擎id',
	`name`	   VARCHAR(100) NOT NULL COMMENT '引擎名称',
    `type` varchar(20) NOT NULL COMMENT '引擎类型 system内置 custom注册',
    `secret` varchar(50) NOT NULL COMMENT '引擎秘钥',
    `status` varchar(50) NOT NULL COMMENT '引擎状态 online在线 offline离线 testing测试中',
    `last_heartbeat_time` bigint(13)  DEFAULT NULL COMMENT '上次心跳时间',
    `project_id` varchar(50) NOT NULL COMMENT '所属项目',
	`update_user`    varchar(50) NOT NULL COMMENT '更新人',
    `create_user`    varchar(50) NOT NULL COMMENT '创建人',
    `create_time` bigint(13)  NOT NULL COMMENT '创建时间',
	`update_time` bigint(13)  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`), INDEX(`project_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `application` (
    `id`       VARCHAR(50) NOT NULL COMMENT '应用id',
	`name`	   VARCHAR(100) NOT NULL COMMENT '应用名称',
	`system` VARCHAR(20) NOT NULL COMMENT '所属系统',
	`app_id` VARCHAR(200) NOT NULL COMMENT '应用标识 安卓的包名 苹果的bundleId',
	`main_activity` VARCHAR(200) DEFAULT NULL COMMENT '应用主页',
	`description` VARCHAR(200) DEFAULT NULL COMMENT '应用描述',
	`status` int(1)  DEFAULT 1 COMMENT '状态 0删除 1正常',
    `project_id`  VARCHAR(50) NOT NULL COMMENT '所属项目id',
    `create_time` bigint(13)  NOT NULL COMMENT '创建时间',
	`update_time` bigint(13)  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`), INDEX(`project_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `device` (
  `id` varchar(50) NOT NULL COMMENT '设备id',
  `serial` varchar(50) NOT NULL COMMENT '设备序列号',
  `name` varchar(100) NOT NULL COMMENT '设备名称',
  `system` varchar(20) NOT NULL COMMENT '设备系统',
  `brand` varchar(20) NOT NULL COMMENT '设备品牌',
  `model` varchar(50) NOT NULL COMMENT '设备型号',
  `version` varchar(20) NOT NULL COMMENT '设备系统版本',
  `sources` json DEFAULT NULL COMMENT '设备资源信息',
  `agent` varchar(50) NOT NULL COMMENT '设备代理id',
  `project_id` varchar(50) DEFAULT NULL COMMENT '设备使用者',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  `update_time` bigint(13) NOT NULL COMMENT '更新时间',
  `status` varchar(20) NOT NULL COMMENT '状态 online在线 offline离线 testing测试中',
  PRIMARY KEY (`id`), UNIQUE(`serial`), INDEX(`system`), INDEX(`agent`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

insert into `engine` (id, name, type, secret, status, last_heartbeat_time, project_id, update_user, create_user, create_time, update_time)
VALUES (REPLACE(UUID(), '-', ''), '系统引擎4', 'system', REPLACE(UUID(), '-', ''), 'offline', NULL, 'system', 'system_admin_user', 'system_admin_user',
REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.',''));

insert into `engine` (id, name, type, secret, status, last_heartbeat_time, project_id, update_user, create_user, create_time, update_time)
VALUES (REPLACE(UUID(), '-', ''), '系统引擎3', 'system', REPLACE(UUID(), '-', ''), 'offline', NULL, 'system', 'system_admin_user', 'system_admin_user',
REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.',''));

insert into `engine` (id, name, type, secret, status, last_heartbeat_time, project_id, update_user, create_user, create_time, update_time)
VALUES (REPLACE(UUID(), '-', ''), '系统引擎2', 'system', REPLACE(UUID(), '-', ''), 'offline', NULL, 'system', 'system_admin_user', 'system_admin_user',
REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.',''));

insert into `engine` (id, name, type, secret, status, last_heartbeat_time, project_id, update_user, create_user, create_time, update_time)
VALUES (REPLACE(UUID(), '-', ''), '系统引擎1', 'system', REPLACE(UUID(), '-', ''), 'offline', NULL, 'system', 'system_admin_user', 'system_admin_user',
REPLACE(unix_timestamp(current_timestamp(3)),'.',''), REPLACE(unix_timestamp(current_timestamp(3)),'.',''));
