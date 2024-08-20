package com.universe.backend.database.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class
TestCaseResponse {
    private String comment; //  用例描述

    private String caseId;  // 用例id

    private String caseName;    // 用例名称

    private JSONArray functions;

    private JSONObject params;
}
