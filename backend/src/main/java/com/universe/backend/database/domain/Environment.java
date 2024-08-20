package com.universe.backend.database.domain;

import lombok.Data;

@Data
public class Environment {
    private String id;

    private String name;

    private String projectId;

    private String description;

    private Long createTime;

    private Long updateTime;

    private String createUser;

    private String updateUser;

    private Integer status;
}
