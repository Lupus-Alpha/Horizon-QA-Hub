package com.universe.backend.database.domain;
import java.io.Serializable;
import lombok.Data;

@Data
public class ReportCollectionCaseApi implements Serializable{
    private String id;

    private String reportCollectionCaseId;

    private Integer index;

    private String apiId;

    private String apiName;

    private String apiPath;

    private String execLog;

    private Integer during;

    private String status;

}
