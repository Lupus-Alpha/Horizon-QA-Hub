package com.universe.backend.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.universe.backend.common.constant.ReportStatus;
import com.universe.backend.database.domain.*;
import com.universe.backend.database.mapper.*;
import com.universe.backend.dto.ReportCollectionCaseDTO;
import com.universe.backend.dto.ReportCollectionDTO;
import com.universe.backend.dto.ReportDTO;
import com.universe.backend.request.CaseResultRequest;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.request.TransResultRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class ReportService {
    @Resource
    private ReportMapper reportMapper;

    @Resource
    private ReportCollectionMapper reportCollectionMapper;

    @Resource
    private ReportCollectionCaseApiMapper reportCollectionCaseApiMapper;

    @Resource
    private ReportCollectionCaseMapper reportCollectionCaseMapper;

    @Resource
    private ReportCollectionCaseWebMapper reportCollectionCaseWebMapper;

    public List<ReportDTO> getReportList(QueryRequest request) {
        if(request.getCondition() != null && !request.getCondition().equals("")){
            request.setCondition(("%"+request.getCondition()+"%"));
        }
        return reportMapper.getReportList(request);
    }

    public ReportDTO getReport(String reportId){
        ReportDTO report = reportMapper.getReportDetail(reportId);
        List<ReportCollectionDTO> reportCollectionList = reportCollectionMapper.getReportCollectionList(reportId);
        for(ReportCollectionDTO reportCollection:reportCollectionList){
            List<ReportCollectionCaseDTO> reportCollectionCaseList = reportCollectionCaseMapper.getReportCollectionCaseList(reportCollection.getId());
            reportCollection.setCaseList(reportCollectionCaseList);
            Integer passCount = reportCollectionCaseMapper.countReportCollectionResult(ReportStatus.SUCCESS.toString(), reportCollection.getId());
            Integer failCount = reportCollectionCaseMapper.countReportCollectionResult(ReportStatus.FAIL.toString(), reportCollection.getId());
            Integer errorCount = reportCollectionCaseMapper.countReportCollectionResult(ReportStatus.ERROR.toString(), reportCollection.getId());
            reportCollection.setPassCount(passCount);
            reportCollection.setFailCount(failCount);
            reportCollection.setErrorCount(errorCount);
        }
        report.setCollectionList(reportCollectionList);
        return report;
    }

    public void updateReport(String reportId,List<CaseResultRequest> caseResultList){
        // 先存case info 基本执行信息到report_collection_case表，再存case中具体事务信息到report_collection_case_api/web表
        for(CaseResultRequest caseResult : caseResultList){ //遍历所有的case结果
            ReportCollection reportCollection = reportCollectionMapper.getReportCollection(reportId, caseResult.getCollectionId());
            String reportCollectionCaseId = reportCollectionCaseMapper.getReportCollectionCaseId(reportCollection.getId(), caseResult.getIndex());
            ReportCollectionCase reportCollectionCase = new ReportCollectionCase();
            reportCollectionCase.setId(reportCollectionCaseId==null? UUID.randomUUID().toString():reportCollectionCaseId);
            reportCollectionCase.setReportCollectionId(reportCollection.getId());
            reportCollectionCase.setIndex(caseResult.getIndex());
            reportCollectionCase.setCaseId(caseResult.getCaseId());
            reportCollectionCase.setCaseType(caseResult.getCaseType());
            reportCollectionCase.setCaseName(caseResult.getCaseName());
            reportCollectionCase.setCaseDesc(caseResult.getCaseDesc());
            reportCollectionCase.setStartTime(caseResult.getStartTime());
            reportCollectionCase.setEndTime(caseResult.getEndTime());
            reportCollectionCase.setDuring((caseResult.getEndTime()-caseResult.getStartTime()) + "ms");
            reportCollectionCase.setStatus(getStatusByIndex(caseResult.getStatus()));
            if(reportCollectionCaseId!=null){ //报告集合用例已存在则更新
                reportCollectionCaseMapper.updateReportCollectionCase(reportCollectionCase);
            }else {
                reportCollectionCaseMapper.addReportCollectionCase(reportCollectionCase);//一条用例一条记录，不存在则新增，存在则更新，一个用例结果集对应同一个报告集合ID，不同的用例索引
            }
            if(caseResult.getCaseType().equals("API")){
                List<ReportCollectionCaseApi> reportCollectionCaseApiList = new ArrayList<>();
                for(int index=1; index <= caseResult.getTransactionList().size(); index++){ //一个用例中包含多个接口事务
                    TransResultRequest transactionResult =caseResult.getTransactionList().get(index-1);
                    ReportCollectionCaseApi reportCollectionCaseApi = new ReportCollectionCaseApi();
                    reportCollectionCaseApi.setId(UUID.randomUUID().toString());
                    reportCollectionCaseApi.setReportCollectionCaseId(reportCollectionCase.getId());
                    reportCollectionCaseApi.setIndex(index);
                    reportCollectionCaseApi.setApiId(transactionResult.getId());
                    reportCollectionCaseApi.setApiName(transactionResult.getName());
                    reportCollectionCaseApi.setApiPath(transactionResult.getContent());
                    reportCollectionCaseApi.setExecLog(transactionResult.getLog());
                    reportCollectionCaseApi.setDuring(transactionResult.getDuring());
                    reportCollectionCaseApi.setStatus(getStatusByIndex(transactionResult.getStatus()));
                    reportCollectionCaseApiList.add(reportCollectionCaseApi);
                }
                if(reportCollectionCaseId!=null){ //报告集合用例已存在则删除原有接口
                    reportCollectionCaseApiMapper.deleteByReportCaseId(reportCollectionCase.getId());
                }
                reportCollectionCaseApiMapper.batchAddReportCollectionCaseApi(reportCollectionCaseApiList);
            }else if(caseResult.getCaseType().equals("WEB")) {// 事务列表 API测试中 一个接口是一个事务 UI测试中 一个操作步骤是一个事务
                List<ReportCollectionCaseWeb> reportCollectionCaseWebList = new ArrayList<>();
                for (int index = 1; index <= caseResult.getTransactionList().size(); index++) {
                    TransResultRequest transactionResult = caseResult.getTransactionList().get(index - 1);
                    ReportCollectionCaseWeb reportCollectionCaseWeb = new ReportCollectionCaseWeb();
                    reportCollectionCaseWeb.setId(UUID.randomUUID().toString());
                    reportCollectionCaseWeb.setReportCollectionCaseId(reportCollectionCase.getId());
                    reportCollectionCaseWeb.setIndex(index);
                    reportCollectionCaseWeb.setOperationId(transactionResult.getId());
                    reportCollectionCaseWeb.setOperationName(transactionResult.getName());
                    reportCollectionCaseWeb.setOperationElement(transactionResult.getContent());
                    reportCollectionCaseWeb.setExecLog(transactionResult.getLog());
                    List<String> screenshot = new ArrayList<>();
                    for (String screenshotId : transactionResult.getScreenShotList()) {
                        String url = "/openapi/screenshot/" + screenshotId + ".png";
                        screenshot.add(url);
                    }
                    reportCollectionCaseWeb.setScreenshot(JSONArray.toJSONString(screenshot));
                    reportCollectionCaseWeb.setStatus(getStatusByIndex(transactionResult.getStatus()));
                    reportCollectionCaseWebList.add(reportCollectionCaseWeb);
                }
                if (reportCollectionCaseId != null) {
                    reportCollectionCaseWebMapper.deleteByReportCaseId(reportCollectionCase.getId());
                }
                reportCollectionCaseWebMapper.batchAddReportCollectionCaseWeb(reportCollectionCaseWebList);
            }else {
                System.out.println("未知的用例类型");
            }

        }
        reportMapper.updateReportEndTime(reportId, System.currentTimeMillis(), System.currentTimeMillis());
        // 统计报告
        this.statisticsReport(reportId);

    }
    public void statisticsReport(String reportId){
        Integer passCount = reportCollectionCaseMapper.countReportResult(ReportStatus.SUCCESS.toString(), reportId);
        Integer failCount = reportCollectionCaseMapper.countReportResult(ReportStatus.FAIL.toString(), reportId);
        Integer errorCount = reportCollectionCaseMapper.countReportResult(ReportStatus.ERROR.toString(), reportId);
        ReportStatistics reportStatistics = new ReportStatistics();
        reportStatistics.setPassCount(passCount);
        reportStatistics.setFailCount(failCount);
        reportStatistics.setErrorCount(errorCount);
        reportStatistics.setId(reportId);
        reportMapper.updateReportStatistics(reportStatistics);
    }

    private String getStatusByIndex(Integer status) {
        if(status == 0){
            return ReportStatus.SUCCESS.toString();
        }else if (status == 1){
            return ReportStatus.FAIL.toString();
        }else{
            return ReportStatus.ERROR.toString();
        }
    }
}
