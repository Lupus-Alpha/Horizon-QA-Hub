package com.universe.backend.database.domain;

import lombok.Data;

@Data
public class ReportCollectionCase {
    private String id;

    private String reportCollectionId;

    private Integer index;

    private String caseId;

    private String caseType;

    private String caseName;

    private String caseDesc;

    private Long startTime;

    private Long endTime;

    private String during;

    private String status;
}
