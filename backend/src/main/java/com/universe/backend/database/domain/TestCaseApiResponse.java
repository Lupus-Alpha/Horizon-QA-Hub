package com.universe.backend.database.domain;

import lombok.Data;

import java.util.List;

@Data
public class TestCaseApiResponse extends TestCaseResponse{
    private List<TestCaseApiDataResponse> apiList;

}
