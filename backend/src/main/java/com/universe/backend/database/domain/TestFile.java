package com.universe.backend.database.domain;

import lombok.Data;

@Data
public class TestFile {
    private String id;

    private String name;

    private String filePath;

    private String description;

    private String projectId;

    private String createUser;

    private String updateUser;

    private Long createTime;

    private Long updateTime;

    private Integer status;
}
