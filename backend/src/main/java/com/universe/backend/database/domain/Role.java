package com.universe.backend.database.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Role implements Serializable {
    private String id;

    private String name;

    private String projectId;

    private Long createTime;

    private Long updateTime;
}
