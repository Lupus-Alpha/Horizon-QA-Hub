package com.universe.backend.database.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskTestCollectionResponse {
    private String collectionId;
    private String deviceId;
    private List<TaskTestCaseResponse> testCaseList;
}
