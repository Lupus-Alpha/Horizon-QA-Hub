package com.universe.backend.database.domain;

import lombok.Data;

@Data
public class Moudle {
    private String id;

    private String name;

    private String parentId;

    private String projectId;

    private Long createTime;

    private Long updateTime;

    private String createUser;

    private String updateUser;

}
