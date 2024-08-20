package com.universe.backend.database.domain;

import lombok.Data;

@Data
public class Operation {
    private String id;

    private String name;

    private String type;

    private String from;

    private String system;

    private String elements;

    private String data;

    private String code;

    private String description;

    private String projectId;

    private String createUser;

    private String updateUser;

    private Long createTime;

    private Long updateTime;

    private Integer status;
}
