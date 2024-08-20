package com.universe.backend.database.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaseWeb implements Serializable {
    private String id;

    private Long index;

    private String caseId;

    private String operationId;

    private String elements;

    private String data;

}
