package com.universe.backend.database.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class TestCaseAppDataResponse {
    private String operationType;

    private String operationSystem; // 操作系统 用来区分当前是apple还是android

    private String operationId;

    private String operationName;

    private String operationTrans;

    private String operationCode;

    private JSONObject operationElement;

    private JSONObject operationData;
}
