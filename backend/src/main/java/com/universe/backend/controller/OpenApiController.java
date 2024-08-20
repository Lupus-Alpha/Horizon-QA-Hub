package com.universe.backend.controller;


import com.universe.backend.database.domain.TaskResponse;
import com.universe.backend.request.EngineRequest;
import com.universe.backend.service.impl.OpenApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/openapi")
public class OpenApiController {

    @Resource
    private OpenApiService openApiService;

    @PostMapping("/engine/token/apply")
    public String applyToken(@RequestBody EngineRequest request) {
        return openApiService.applyToken(request);
    }

    @PostMapping("/engine/heartbeat/send")
    public void sendHeartbeat(@RequestBody EngineRequest request) {
        openApiService.sendHeartbeat(request);
    }

    @PostMapping("/engine/task/fetch")  // 引擎获取任务接口
    public TaskResponse fetchEngineTask(@RequestBody EngineRequest request) {
        return openApiService.fetchEngineTask(request);
    }

    @GetMapping("/task/file/download/{taskId}") // 引擎下载任务文件
    public void downloadTaskFile(@PathVariable String taskId, HttpServletResponse response) {
        openApiService.downTaskFile(taskId, response);
    }

    @PostMapping("/engine/result/upload")   // 引擎上报用例结果
    public void uploadCaseResult(@RequestBody EngineRequest request) {
        openApiService.uploadCaseResult(request);
    }

    @PostMapping("/engine/task/complete")   // 引擎上报任务完成接口
    public void completeEngineTask(@RequestBody EngineRequest request) {
        openApiService.completeEngineTask(request);
    }



    }
