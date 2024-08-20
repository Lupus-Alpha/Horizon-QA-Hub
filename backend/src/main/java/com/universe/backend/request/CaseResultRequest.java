package com.universe.backend.request;

import lombok.Data;

import java.util.List;

@Data
public class CaseResultRequest {
    private Integer status;

    private Long startTime;

    private Long endTime;

    private String collectionId;

    private String caseId;

    private String caseType;

    private String caseName;

    private String caseDesc;

    private Integer index;  // 在集合中的顺序

    private List<TransResultRequest> transactionList;
}
