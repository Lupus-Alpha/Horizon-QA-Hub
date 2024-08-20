package com.universe.backend.database.domain;

import lombok.Data;

@Data
public class Device {
    private String id;

    private String serial;

    private String name;

    private String system;

    private String brand;

    private String model;

    private String version;

    private String sources;

    private String agent;

    private String projectId;

    private Long createTime;

    private Long updateTime;

    private String status;
}
