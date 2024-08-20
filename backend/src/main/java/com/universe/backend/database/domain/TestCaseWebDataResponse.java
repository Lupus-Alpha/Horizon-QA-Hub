package com.universe.backend.database.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class TestCaseWebDataResponse {
    private String operationType;

    private String operationId;

    private String operationName;   // 操作名称 主要用来映射引擎的代码 自定义操作时该字段即自定义

    private String operationTrans;  // 真正的操作名称

    private String operationCode;

    private JSONObject operationElement;

    private JSONObject operationData;
}
