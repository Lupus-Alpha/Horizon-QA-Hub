package com.universe.backend.database.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class TaskResponse {
    private String taskId;
    private String taskType;
    private String downloadUrl;
    private Boolean reRun;
    private JSONObject debugData;
    private List<TaskTestCollectionResponse> testCollectionList;

}
