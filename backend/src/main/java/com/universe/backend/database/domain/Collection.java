package com.universe.backend.database.domain;

import lombok.Data;

@Data
public class Collection {
    private String id;
    private String name;
    private String deviceId;
    private String description;
    private String projectId;
    private Long createTime;
    private Long updateTime;
    private String createUser;
    private String updateUser;
    private Integer status;
    private Integer total;

}
