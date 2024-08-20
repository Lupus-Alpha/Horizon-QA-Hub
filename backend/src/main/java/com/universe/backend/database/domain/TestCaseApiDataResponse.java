package com.universe.backend.database.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class TestCaseApiDataResponse {
    private String apiId;

    private String apiName;

    private String url; // 请求域名

    private String path;

    private String method;

    private String protocol;

    private JSONObject headers;

    private JSONObject proxies;

    private JSONObject body;

    private JSONObject query;

    private JSONObject rest;

    private JSONArray assertions;

    private JSONArray relations;

    private JSONObject controller;
}
