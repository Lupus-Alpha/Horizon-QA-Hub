package com.universe.backend.database.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Application implements Serializable {
    private String id;

    private String name;

    private String system;

    private String appId;   // packageName

    private String mainActivity;

    private String description;

    private String projectId;

    private Long createTime;

    private Long updateTime;

    private Integer status;

}
