package com.universe.backend.database.domain;

import lombok.Data;

@Data   // 定义数据表实体类 用来接收数据库数据
public class User {

    private String id;

    private String username;

    private String account;

    private String password;

    private String mobile;

    private String email;

    private Long createTime;

    private Long updateTime;

    private String lastProject; // 记录用户最后访问的项目id

    private Integer status;

}
