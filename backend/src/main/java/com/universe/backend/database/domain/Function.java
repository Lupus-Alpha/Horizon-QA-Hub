package com.universe.backend.database.domain;

import lombok.Data;

@Data
public class Function {
    private String id;

    private String name;

    private String from;

    private String params;

    private String code;

    private String description;

    private String projectId;

    private String createUser;

    private String updateUser;

    private Long createTime;

    private Long updateTime;

    private Integer status;
}
