package com.universe.backend.database.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Report implements Serializable {
    private String id;

    private String name;

    private String engineId;

    private String environmentId;

    private String deviceId;

    private Integer sourceType;

    private String sourceId;

    private Long startTime;

    private Long endTime;

    private String status;

    private String projectId;

    private Long createTime;

    private Long updateTime;

    private String createUser;

    private String updateUser;

    private Integer total;

    private Integer passCount;

    private Integer failCount;

    private Integer errorCount;

}
