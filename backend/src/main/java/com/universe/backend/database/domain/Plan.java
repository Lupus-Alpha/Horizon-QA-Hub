package com.universe.backend.database.domain;

import lombok.Data;

@Data
public class Plan {
    private String id;

    private String name;

    private String description;

    private String environmentId;

    private String retry;

    private String engineId;

    private String projectId;

    private Long createTime;

    private Long updateTime;

    private String createUser;

    private String updateUser;

    private Integer status;

}
