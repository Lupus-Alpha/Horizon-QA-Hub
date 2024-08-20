package com.universe.backend.request;

import lombok.Data;

@Data
public class QueryRequest {
    private String condition;
    private String user_id;
    private String projectId;
    private String operationType;
    private String system;
    private String uiType;
    private String groupName;
    private String status;
    private String moduleId;
    private String caseType;
    private String collectionId;

}
