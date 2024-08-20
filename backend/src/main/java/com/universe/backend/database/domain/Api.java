package com.universe.backend.database.domain;

import lombok.Data;

@Data
public class Api {
    private String id;

    private Long num;

    private String name;

    private String moduleId;

    private String projectId;

    private String method;

    private String path;

    private String protocol;

    private String serverSign;

    private String description;

    private String header;

    private String body;

    private String query;

    private String rest;

    private Long createTime;

    private Long updateTime;

    private String createUser;

    private String updateUser;

    private Integer status;

}
