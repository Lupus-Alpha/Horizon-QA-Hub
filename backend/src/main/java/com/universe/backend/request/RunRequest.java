package com.universe.backend.request;

import lombok.Data;

@Data
public class RunRequest {
    private String  id;
    private String  environmentId;
    private String  deviceId;
    private Integer sourceType;
    private String  sourceId;
    private String  sourceName;
    private String  runUser;
    private String  projectId;
    private String  engineId;
    private String  debugData;

}
