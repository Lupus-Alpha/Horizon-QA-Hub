package com.universe.backend.service.impl;



import com.universe.backend.common.constant.EngineStatus;
import com.universe.backend.common.constant.ReportStatus;
import com.universe.backend.database.domain.*;
import com.universe.backend.database.mapper.EngineMapper;
import com.universe.backend.database.mapper.ReportMapper;
import com.universe.backend.request.CaseResultRequest;
import com.universe.backend.request.EngineRequest;
import com.universe.backend.utils.FileUtils;
import com.universe.backend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;

@Service
public class OpenApiService {
    @Resource
    private EngineMapper engineMapper;

    @Resource
    private ReportMapper reportMapper;

    @Resource
    private RunService runService;

    @Resource
    private ReportService reportService;

    @Value("${task.file.path}")
    private String TASK_FILE_PATH;

    public String applyToken(EngineRequest request) {
        Engine engine = new Engine();
        engine.setId(request.getEngineCode());
        engine.setSecret(request.getEngineSecret());
        return JwtUtils.createEngineToken(engine);

    }

    public void sendHeartbeat(EngineRequest request) {
        engineMapper.updateHeartbeat(request.getTimestamp(),request.getEngineCode());
        Engine engine = engineMapper.getEngineById(request.getEngineCode());
        if(engine.getStatus().equals(EngineStatus.OFFLINE.toString().toLowerCase(Locale.ROOT))){
            engineMapper.updateStatus(request.getEngineCode(), EngineStatus.ONLINE.toString().toLowerCase(Locale.ROOT));
        }

    }

    public TaskResponse fetchEngineTask(EngineRequest request) {
        Engine engine = engineMapper.getEngineById(request.getEngineCode());
        if (engine==null){
            throw new RuntimeException("引擎code不存在");
        }
        Report report = reportMapper.getToRunReport(engine.getId());
        if(report==null){
            return null;
        }
        report.setCreateTime(System.currentTimeMillis());
        report.setUpdateTime(System.currentTimeMillis());
        report.setStatus(ReportStatus.RUNNING.toString());
        report.setEngineId(engine.getId());
        reportMapper.updateReportToRunning(report);
        engineMapper.updateStatus(engine.getId(), EngineStatus.TESTING.toString().toLowerCase(Locale.ROOT));
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setTaskId(report.getId());
        taskResponse.setTaskType(report.getSourceType()<=1?"DEBUG":"BATCH");
        taskResponse.setReRun(false);
        List<TaskTestCollectionResponse> testCollectionList = runService.getTaskTestCollections(report);
        taskResponse.setTestCollectionList(testCollectionList);
        if(report.getSourceType() <= 1){
            taskResponse.setDebugData(runService.getCaseJsonData(report.getEnvironmentId(), report.getDeviceId(),
                    report.getSourceType(), testCollectionList.get(0).getTestCaseList().get(0)));
            taskResponse.setDownloadUrl(null);
        }else {
            taskResponse.setDownloadUrl(runService.getDownloadUrl(report, testCollectionList));
            taskResponse.setDebugData(null);
        }

        return taskResponse;
    }

    public void completeEngineTask(EngineRequest request){
        // 统计报告信息
        ReportStatistics reportStatistics = reportMapper.getReportStatistics(request.getTaskId());
        String reportStatus;
        if(reportStatistics.getErrorCount() > 0){
            reportStatus = ReportStatus.ERROR.toString();
        }else if(reportStatistics.getFailCount() > 0){
            reportStatus = ReportStatus.FAIL.toString();
        }else if(reportStatistics.getPassCount() > 0){
            reportStatus = ReportStatus.SUCCESS.toString();
        }else {
            reportStatus = ReportStatus.COMPLETED.toString();
        }
        engineMapper.updateStatus(request.getEngineCode(), EngineStatus.ONLINE.toString().toLowerCase(Locale.ROOT)); // 恢复引擎为在线状态
        reportMapper.updateReportStatus(reportStatus, request.getTaskId()); // 更新报告状态
        reportMapper.updateReportEndTime(request.getTaskId(), System.currentTimeMillis(), System.currentTimeMillis());
        // 释放设备
//        runService.stopDeviceWhenRunEnd(request.getTaskId());
        // 删除任务文件 并通知执行人

    }

    public void downTaskFile(String reportId, HttpServletResponse response) {
        Report report = reportMapper.getReportById(reportId);
        if(report==null){
            throw new RuntimeException("任务不存在");
        }
        String path = TASK_FILE_PATH + "/"+report.getProjectId()+"/"+reportId+".zip";
        FileUtils.downloadFile(response,path);
    }

    public void uploadCaseResult(EngineRequest request) {
        List<CaseResultRequest> caseResultList = request.getCaseResultList();
        reportService.updateReport(request.getTaskId(), caseResultList);
    }

}
