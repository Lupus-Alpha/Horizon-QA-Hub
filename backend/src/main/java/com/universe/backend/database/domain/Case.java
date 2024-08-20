package com.universe.backend.database.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Case implements Serializable {
    private String id;

    private Long num;

    private String name;

    private String moduleId;

    private String projectId;

    private String type;

    private String description;

    private String system;

    private String commonParam;

    private Long createTime;

    private Long updateTime;

    private String createUser;

    private String updateUser;

    private Integer status;

}
