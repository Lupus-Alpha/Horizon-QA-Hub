package com.universe.backend.database.domain;

import lombok.Data;

import java.util.List;

@Data
public class TestCaseAppResponse extends TestCaseResponse{
    private String appId;   // 需要打开的包名

    private String activity;    // 安卓打开时打开的页面

    private String deviceSystem;    // 设备系统

    private String deviceUrl;   // 设备url

    private List<TestCaseAppDataResponse> optList; // APP用例的操作列表
}
