package com.universe.backend.database.domain;

import lombok.Data;

@Data
public class Element {
    private String id;

    private Long num;

    private String name;

    private String moduleId;

    private String projectId;

    private String by;

    private String expression;

    private String description;

    private Long createTime;

    private Long updateTime;

    private String createUser;

    private String updateUser;

    private Integer status;
}
