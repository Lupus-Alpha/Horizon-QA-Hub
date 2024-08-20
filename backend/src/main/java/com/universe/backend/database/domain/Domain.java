package com.universe.backend.database.domain;

import lombok.Data;

@Data   // 定义数据表实体类 用来接收数据库数据
public class Domain {

    private String id;

    private String type;

    private String domainKey;

    private String domainValue;

    private String environmentId;

    private Long createTime;

    private Long updateTime;

    private String createUser;

    private String updateUser;

}
