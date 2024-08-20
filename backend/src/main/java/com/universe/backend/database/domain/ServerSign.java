package com.universe.backend.database.domain;

import lombok.Data;

@Data   // 定义数据表实体类 用来接收数据库数据
public class ServerSign {

    private String id;

    private String name;

    private String description;

    private String projectId;

    private Long createTime;

    private Long updateTime;

    private Integer status;
}
