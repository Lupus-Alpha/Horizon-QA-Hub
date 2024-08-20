package com.universe.backend.request;

import lombok.Data;

import java.util.List;

@Data
public class EngineRequest {
    private String engineCode;

    private String engineSecret;

    private Long timestamp;

    private String taskId;

    private String taskType;

    private String fileName;    // 截图文件名即uuid

    private String base64String;    // 截图的base64内容

    private List<CaseResultRequest> caseResultList;



}
