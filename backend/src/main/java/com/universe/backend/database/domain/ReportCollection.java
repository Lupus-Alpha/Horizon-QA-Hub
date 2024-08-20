package com.universe.backend.database.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReportCollection implements Serializable {
    private String id;

    private String reportId;

    private String collectionId;

    private String collectionName;

}
