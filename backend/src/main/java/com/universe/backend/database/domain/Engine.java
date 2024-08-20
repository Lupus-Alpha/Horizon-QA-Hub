package com.universe.backend.database.domain;

import lombok.Data;

@Data
public class Engine {
    private String id;

    private String name;

    private String type;

    private String secret;

    private String status;

    private Long lastHeartbeatTime;

    private String projectId;

    private String createUser;

    private String updateUser;

    private Long createTime;

    private Long updateTime;
}
