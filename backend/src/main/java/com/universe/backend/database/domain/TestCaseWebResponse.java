package com.universe.backend.database.domain;

import lombok.Data;

import java.util.List;

@Data
public class TestCaseWebResponse extends TestCaseResponse{
    private Boolean startDriver;

    private Boolean closeDriver;

    private List<TestCaseWebDataResponse> optList; // WEB用例的操作列表
}
