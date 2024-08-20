package com.universe.backend.database.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReportCollectionCaseWeb implements Serializable {
    private String id;

    private String reportCollectionCaseId;

    private Integer index;

    private String operationId;

    private String operationName;

    private String operationElement;

    private String screenshot;

    private String execLog;

    private String status;

}
