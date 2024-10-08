-- create table report
-- (
--     `id`       VARCHAR(50) NOT NULL COMMENT '报告id',
--     `name`	   VARCHAR(150) NOT NULL COMMENT '报告名称',
--     `engine_id`  VARCHAR(50) NOT NULL COMMENT '执行引擎id',
--     `environment_id`  VARCHAR(50) DEFAULT NULL COMMENT '执行环境id',
--     `device_id`  VARCHAR(50) DEFAULT NULL COMMENT '执行设备id',
--     `source_type` int(1) NOT NULL COMMENT '关联计划/集合/用例/调试',
--     `source_id`  VARCHAR(50) NOT NULL COMMENT '关联计划/集合/用例id/调试id',
--     `start_time`  bigint(13) DEFAULT NULL COMMENT '开始时间',
--     `end_time`  bigint(13) DEFAULT NULL COMMENT '结束时间',
--     `status`  VARCHAR(20) NOT NULL COMMENT '执行状态',
--     `project_id`  VARCHAR(50) NOT NULL COMMENT '所属项目id',
--     `update_user`    varchar(50) NOT NULL COMMENT '更新人',
--     `create_user`    varchar(50) NOT NULL COMMENT '创建人',
--     `create_time` bigint(13)  NOT NULL COMMENT '创建时间',
--     `update_time` bigint(13)  NOT NULL COMMENT '更新时间',
--     `pass_count` int(8) NOT NULL COMMENT '报告成功用例数',
--     `fail_count` int(8) NOT NULL COMMENT '报告失败用例数',
--     `error_count` int(8) NOT NULL COMMENT '报告错误用例数',
--     PRIMARY KEY (`id`), INDEX(`source_id`), INDEX(`project_id`), INDEX(`source_type`), INDEX(`engine_id`)
-- ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

-- CREATE TABLE `report_collection` (
--                                      `id`       VARCHAR(50) NOT NULL COMMENT '报告集合id',
--                                      `report_id` VARCHAR(50) NOT NULL COMMENT '报告id',
--                                      `collection_id` VARCHAR(50) NOT NULL COMMENT '集合id',
--                                      `collection_name` VARCHAR(100) DEFAULT NULL COMMENT '集合名称',
--                                      PRIMARY KEY (`id`), INDEX(`report_id`)
-- ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `debug_data` (
                              `id`       VARCHAR(50) NOT NULL COMMENT '调试数据id',
                              `data`	   json NOT NULL COMMENT '调试数据',
                              PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `report_collection_case` (
                                          `id`       VARCHAR(50) NOT NULL COMMENT '报告集合用例id',
                                          `report_collection_id` VARCHAR(50) NOT NULL COMMENT '报告集合id',
                                          `index` int(8) NOT NULL COMMENT '集合用例序号',
                                          `case_id` VARCHAR(50) NOT NULL COMMENT '用例id',
                                          `case_type`	VARCHAR(20) NOT NULL COMMENT '用例类型',
                                          `case_name` VARCHAR(100) NOT NULL COMMENT '用例名',
                                          `case_desc` VARCHAR(200) DEFAULT NULL COMMENT '用例描述',
                                          `start_time`  bigint(13) NOT NULL COMMENT '开始时间',
                                          `end_time`  bigint(13) NOT NULL COMMENT '结束时间',
                                          `during`  VARCHAR(10) NOT NULL COMMENT '执行时长',
                                          `status` VARCHAR(10) NOT NULL COMMENT '执行结果',
                                          PRIMARY KEY (`id`), INDEX(`report_collection_id`), INDEX(`index`), INDEX(`case_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `report_collection_case_api` (
                                              `id`       VARCHAR(50) NOT NULL COMMENT '报告集合用例接口id',
                                              `report_collection_case_id` VARCHAR(50) NOT NULL COMMENT '报告集合用例id',
                                              `index` int(8) NOT NULL COMMENT '用例接口序号',
                                              `api_id` VARCHAR(50) NOT NULL COMMENT '接口id',
                                              `api_name` VARCHAR(100) NOT NULL COMMENT '接口名称',
                                              `api_path`	VARCHAR(200) NOT NULL COMMENT '接口地址',
                                              `exec_log` text DEFAULT NULL COMMENT '执行日志',
                                              `during` int(8) NOT NULL COMMENT '接口执行时长',
                                              `status` VARCHAR(10) NOT NULL COMMENT '执行结果',
                                              PRIMARY KEY (`id`), INDEX(`report_collection_case_id`), INDEX(`api_id`), INDEX(`index`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `report_collection_case_web` (
                                              `id`       VARCHAR(50) NOT NULL COMMENT '报告集合用例操作id',
                                              `report_collection_case_id` VARCHAR(50) NOT NULL COMMENT '报告集合用例id',
                                              `index` int(8) NOT NULL COMMENT '用例操作序号',
                                              `operation_id` VARCHAR(50) NOT NULL COMMENT '操作id',
                                              `operation_name` VARCHAR(100) NOT NULL COMMENT '操作名称',
                                              `operation_element` text DEFAULT NULL COMMENT '操作元素',
                                              `screen_shot` json DEFAULT NULL COMMENT '操作截图地址',
                                              `exec_log` text DEFAULT NULL COMMENT '执行日志',
                                              `status` VARCHAR(10) NOT NULL COMMENT '执行结果',
                                              PRIMARY KEY (`id`), INDEX(`report_collection_case_id`), INDEX(`operation_id`), INDEX(`index`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `report_collection_case_app` (
                                              `id`       VARCHAR(50) NOT NULL COMMENT '报告集合用例操作id',
                                              `report_collection_case_id` VARCHAR(50) NOT NULL COMMENT '报告集合用例id',
                                              `index` int(8) NOT NULL COMMENT '用例操作序号',
                                              `operation_id` VARCHAR(50) NOT NULL COMMENT '操作id',
                                              `operation_name` VARCHAR(100) NOT NULL COMMENT '操作名称',
                                              `operation_element` text DEFAULT NULL COMMENT '操作元素',
                                              `screen_shot` json DEFAULT NULL COMMENT '操作截图地址',
                                              `exec_log` text DEFAULT NULL COMMENT '执行日志',
                                              `status` VARCHAR(10) NOT NULL COMMENT '执行结果',
                                              PRIMARY KEY (`id`), INDEX(`report_collection_case_id`), INDEX(`operation_id`), INDEX(`index`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;
