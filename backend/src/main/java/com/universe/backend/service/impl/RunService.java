package com.universe.backend.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.universe.backend.common.constant.DeviceStatus;
import com.universe.backend.common.constant.ReportStatus;
import com.universe.backend.common.constant.SourceType;
import com.universe.backend.common.exception.DemoException;
import com.universe.backend.database.domain.*;
import com.universe.backend.database.mapper.*;
import com.universe.backend.dto.*;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.request.RunRequest;
import com.universe.backend.utils.FileUtils;
import com.universe.backend.utils.ZipUtils;
import lombok.Data;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RunService {
    @Resource
    private CollectionMapper collectionMapper;
    @Resource
    private planCollectionMapper planCollectionMapper;
    @Resource
    private ReportMapper reportMapper;
    @Resource
    private ReportCollectionMapper reportCollectionMapper;

    @Resource
    private CollectionCaseMapper collectionCaseMapper;

    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private CaseMapper caseMapper;

    @Resource
    private CaseAppMapper caseAppMapper;

    @Resource
    private OperationMapper operationMapper;

    @Resource
    private ParamsMapper commonParamMapper;

    @Resource
    private CaseWebMapper caseWebMapper;

    @Resource
    private ElementMapper elementMapper;

    @Resource
    private DebugMapper debugDataMapper;

    @Resource
    private CaseApiMapper caseApiMapper;

    @Resource
    private ControlMapper controlMapper;

    @Resource
    private FunctionMapper functionMapper;

    @Resource
    private ParamsMapper paramsMapper;

    @Resource
    private ApplicationMapper applicationMapper;

    @Resource
    private DomainMapper domainMapper;

    @Value("${task.file.path}")
    private String TASK_FILE_PATH;

    public String run(RunRequest runRequest) {
        Report report = new Report();
        report.setId(UUID.randomUUID().toString());
        report.setCreateUser(runRequest.getRunUser());
        report.setUpdateUser(runRequest.getRunUser());
        report.setCreateTime(System.currentTimeMillis());
        report.setUpdateTime(System.currentTimeMillis());
        report.setProjectId(runRequest.getProjectId());
        report.setDeviceId(runRequest.getDeviceId());
        report.setEngineId(runRequest.getEngineId());
        report.setSourceId(runRequest.getSourceId());
        report.setStatus(ReportStatus.PREPARED.toString());
        report.setSourceType(runRequest.getSourceType());
        report.setName(runRequest.getSourceName()+"-"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        report.setDeviceId(runRequest.getDeviceId());
        report.setEnvironmentId(runRequest.getEnvironmentId());
        report.setErrorCount(0);
        report.setFailCount(0);
        report.setPassCount(0);
        List<ReportCollection> reportCollections = new ArrayList<>();
        if(runRequest.getSourceType().equals(SourceType.PLAN.getValue())){ //计划中包含多个集合
            List<CollectionDTO> collectionDTOS = planCollectionMapper.getPlanCollections(runRequest.getSourceId());
            for(CollectionDTO collectionDTO:collectionDTOS){
                report.setTotal(collectionDTO.getCaseCount()+report.getTotal());
                ReportCollection reportCollection = new ReportCollection();
                reportCollection.setId(UUID.randomUUID().toString());
                reportCollection.setReportId(report.getId());
                reportCollection.setCollectionId(collectionDTO.getId());
                reportCollection.setCollectionName(collectionDTO.getName());
                reportCollections.add(reportCollection);
            }
        }else if(runRequest.getSourceType().equals(SourceType.COLLECTION.getValue())){
            CollectionDTO collectionDTO = collectionMapper.getCollectionDetail(runRequest.getSourceId());
            report.setTotal(collectionDTO.getCaseCount());
            ReportCollection reportCollection = new ReportCollection();
            reportCollection.setId(UUID.randomUUID().toString());
            reportCollection.setReportId(report.getId());
            reportCollection.setCollectionId(runRequest.getSourceId());
            reportCollection.setCollectionName(runRequest.getSourceName());
            reportCollections.add(reportCollection);
        }else {
            report.setTotal(1);
            ReportCollection reportCollection = new ReportCollection();
            reportCollection.setId(UUID.randomUUID().toString());
            reportCollection.setReportId(report.getId());
            reportCollection.setCollectionId(runRequest.getSourceId());
            reportCollection.setCollectionName(runRequest.getSourceName());
            reportCollections.add(reportCollection);
        }
        reportMapper.addReport(report);
        reportCollectionMapper.addReportCollection(reportCollections);
        return report.getId();
    }

    public List<TaskTestCollectionResponse> getTaskTestCollections(Report report){
        List<TaskTestCollectionResponse> taskTestCollectionResponses = new ArrayList<>();
        if(report.getSourceType().equals(SourceType.PLAN.getValue())){
            List<CollectionDTO> collectionDTOS = planCollectionMapper.getPlanCollections(report.getSourceId());
            for(CollectionDTO collectionDTO:collectionDTOS){
                TaskTestCollectionResponse taskTestCollectionResponse = new TaskTestCollectionResponse();
                taskTestCollectionResponse.setCollectionId(collectionDTO.getId());
                if(getDeviceStatus(report.getDeviceId())) {
                    taskTestCollectionResponse.setDeviceId(report.getDeviceId());
                }else {
                    taskTestCollectionResponse.setDeviceId(null);
                }
                taskTestCollectionResponse.setDeviceId(report.getDeviceId());
                List<TaskTestCaseResponse> taskTestCaseResponses = getTaskTestCaseList(collectionDTO.getId());
                taskTestCollectionResponse.setTestCaseList(taskTestCaseResponses);
                taskTestCollectionResponses.add(taskTestCollectionResponse);
            }
        }else if(report.getSourceType().equals(SourceType.COLLECTION.getValue())) {
            TaskTestCollectionResponse taskTestCollectionResponse = new TaskTestCollectionResponse();
            taskTestCollectionResponse.setCollectionId(report.getSourceId());
            if (getDeviceStatus(report.getDeviceId())) {
                taskTestCollectionResponse.setDeviceId(report.getDeviceId());
            } else {
                taskTestCollectionResponse.setDeviceId(null);
            }
            List<TaskTestCaseResponse> taskTestCaseResponses = getTaskTestCaseList(report.getSourceId());
            taskTestCollectionResponse.setTestCaseList(taskTestCaseResponses);
            taskTestCollectionResponses.add(taskTestCollectionResponse);
        }else if(report.getSourceType().equals(SourceType.CASE.getValue())){
            CaseDTO caseDTO = caseMapper.getCaseById(report.getSourceId());
            TaskTestCollectionResponse taskTestCollectionResponse = new TaskTestCollectionResponse();
            taskTestCollectionResponse.setCollectionId(report.getSourceId());
            if(getDeviceStatus(report.getDeviceId())) {
                taskTestCollectionResponse.setDeviceId(report.getDeviceId());
            }else {
                taskTestCollectionResponse.setDeviceId(null);
            }
            List<TaskTestCaseResponse> taskTestCaseResponses = new ArrayList<>();
            TaskTestCaseResponse taskTestCaseResponse = new TaskTestCaseResponse();
            taskTestCaseResponse.setCaseId(caseDTO.getId());
            taskTestCaseResponse.setCaseType(caseDTO.getType());
            taskTestCaseResponse.setIndex(1L);
            taskTestCaseResponses.add(taskTestCaseResponse);
            taskTestCollectionResponse.setTestCaseList(taskTestCaseResponses);
            taskTestCollectionResponses.add(taskTestCollectionResponse);
        }else {
            String data = debugDataMapper.getDebugData(report.getSourceId());
            CaseDTO caseDTO = JSONObject.parseObject(data, CaseDTO.class);
            TaskTestCollectionResponse taskTestCollection = new TaskTestCollectionResponse();
            taskTestCollection.setCollectionId(report.getSourceId());
            if(getDeviceStatus(report.getDeviceId())){
                taskTestCollection.setDeviceId(report.getDeviceId());
            }else {
                taskTestCollection.setDeviceId(null);
            }
            List<TaskTestCaseResponse> taskTestCaseList = new ArrayList<>();
            TaskTestCaseResponse taskTestCase = new TaskTestCaseResponse();
            taskTestCase.setIndex(1L);
            taskTestCase.setCaseId(report.getSourceId());
            taskTestCase.setCaseType(caseDTO.getType());
            taskTestCaseList.add(taskTestCase);
            taskTestCollection.setTestCaseList(taskTestCaseList);
            taskTestCollectionResponses.add(taskTestCollection);
        }

        return taskTestCollectionResponses;
    }
    private List<TaskTestCaseResponse> getTaskTestCaseList(String collectionId){
        // 获取任务集合下的用例列表
        List<CollectionCaseDTO> collectionCases = collectionCaseMapper.getCollectionCaseList(collectionId);
        List<TaskTestCaseResponse> taskTestCaseList = new ArrayList<>();
        for(CollectionCaseDTO collectionCaseDTO:collectionCases){
            TaskTestCaseResponse taskTestCase = new TaskTestCaseResponse();
            taskTestCase.setIndex(collectionCaseDTO.getIndex());
            taskTestCase.setCaseId(collectionCaseDTO.getCaseId());
            taskTestCase.setCaseType(collectionCaseDTO.getCaseType());
            taskTestCaseList.add(taskTestCase);
        }
        return taskTestCaseList;
    }
    private Boolean getDeviceStatus(String deviceId){
        if(null == deviceId){
            return false;
        }
        Device device = deviceMapper.getDeviceById(deviceId);
        if(!device.getStatus().equals(DeviceStatus.ONLINE.toString().toLowerCase(Locale.ROOT))){
            // 如果当前设备不是在线状态 则不能执行
            return false;
        }
        device.setStatus(DeviceStatus.TESTING.toString().toLowerCase(Locale.ROOT));
        device.setUpdateTime(System.currentTimeMillis());
        deviceMapper.updateDevice(device);  // 占用设备
        return true;
    }

    public String getDownloadUrl(Report report,List<TaskTestCollectionResponse> testCollectionList){
        String taskFilePath = TASK_FILE_PATH+"/"+report.getProjectId()+"/"+report.getId();  // 保存一个个json文件的地址
        String taskZipPath = TASK_FILE_PATH+"/"+report.getProjectId();
        for(TaskTestCollectionResponse testCollection:testCollectionList){
            String collectionFilePath = taskFilePath +"/"+ testCollection.getCollectionId();
            for(TaskTestCaseResponse taskTestCase:testCollection.getTestCaseList()) { //每一个集合生成一个json文件
                    JSONObject caseJsonData = getCaseJsonData(report.getEnvironmentId(), report.getDeviceId(), report.getSourceType(), taskTestCase);
                    FileUtils.createJsonFile(caseJsonData, collectionFilePath + "/" + taskTestCase.getCaseId() + ".json");

                }
            }
        try {
            ZipUtils.compress(taskFilePath, taskZipPath , report.getId());
            FileUtils.delFile(taskFilePath);
            return "/task/file/download/"+ report.getId();
        }catch (Exception e){
            throw new DemoException("json文件压缩失败");
        }


    }
    public JSONObject getCaseJsonData(String environmentId, String deviceId, Integer sourceType, TaskTestCaseResponse taskTestCase){
        if(taskTestCase.getCaseType().equals("API")) {
            TestCaseApiResponse testCase = getApiTestCaseJson(environmentId, sourceType, taskTestCase);
            return (JSONObject) JSONObject.toJSON(testCase);
        }else if(taskTestCase.getCaseType().equals("WEB")) {
            TestCaseWebResponse testCase = this.getWebTestCaseJson(environmentId, sourceType, taskTestCase);
            return (JSONObject) JSONObject.toJSON(testCase);
        }else{
            TestCaseAppResponse testCase = this.getAppTestCaseJson(deviceId, sourceType, taskTestCase);
            return (JSONObject) JSONObject.toJSON(testCase);
        }

    }

    private TestCaseAppResponse getAppTestCaseJson(String deviceId, Integer sourceType, TaskTestCaseResponse taskTestCase) {
        TestCaseAppResponse testCaseAppResponse = new TestCaseAppResponse();
        CaseDTO caseDTO;
        if(sourceType.equals(SourceType.TEMP.getValue())) {
            String debugData = debugDataMapper.getDebugData(taskTestCase.getCaseId());
            caseDTO = JSONObject.parseObject(debugData, CaseDTO.class);
        }else {
            caseDTO = caseMapper.getCaseDetail(taskTestCase.getCaseType(),taskTestCase.getCaseId());
            caseDTO.setCaseApps(caseAppMapper.getCaseList(taskTestCase.getCaseId()));
        }
        testCaseAppResponse.setCaseId(caseDTO.getId());
        testCaseAppResponse.setCaseName(caseDTO.getName());
        JSONObject commonParam = JSONObject.parseObject(caseDTO.getCommonParam());
        // 组装自定义函数
        testCaseAppResponse.setFunctions(this.getCaseFunctions(commonParam.getJSONArray("functions")));
        // 组装用例公参
        testCaseAppResponse.setParams(this.getCaseParams(commonParam.getJSONArray("params")));
        testCaseAppResponse.setComment(caseDTO.getDescription());
        List<TestCaseAppDataResponse> optList = new ArrayList<>();

        Application application = applicationMapper.getApplicationById(commonParam.getString("appId"));
        testCaseAppResponse.setAppId(application.getAppId());
        if(commonParam.getString("activity")!=null &&
                !commonParam.getString("activity").equals("")){
            testCaseAppResponse.setActivity(commonParam.getString("activity"));
        }else {
            testCaseAppResponse.setActivity(application.getMainActivity());
        }
        // 组装设备信息
        Device device = deviceMapper.getDeviceById(deviceId);
        testCaseAppResponse.setDeviceSystem(device.getSystem());
        if(device.getSystem().equals("android")) {
            testCaseAppResponse.setDeviceUrl(JSONObject.parseObject(device.getSources()).getString("atxUrl"));
        }else {
            testCaseAppResponse.setDeviceUrl(JSONObject.parseObject(device.getSources()).getString("wdaUrl"));
        }

        for(CaseAppDTO caseAppDTO:caseDTO.getCaseApps()){
            TestCaseAppDataResponse appDataResponse = new TestCaseAppDataResponse();
            Operation operation = operationMapper.getOperationDetail(caseAppDTO.getOperationId());
            if(operation.getFrom().equals("custom")){
                appDataResponse.setOperationName("自定义");
                appDataResponse.setOperationCode(operation.getCode());
            }else {
                appDataResponse.setOperationName(operation.getName());
                appDataResponse.setOperationCode(null);
            }

            appDataResponse.setOperationId(caseAppDTO.getOperationId());
            appDataResponse.setOperationElement(this.getAppElement(caseAppDTO.getElements()));
            appDataResponse.setOperationData(this.getAppData(caseAppDTO.getData()));
            optList.add(appDataResponse);
        }
        testCaseAppResponse.setOptList(optList);

        return testCaseAppResponse;
    }
    private JSONObject getAppData(String data){
        //{paramName:{type:xx,value:xx},paramName:{type:xx,value:xx}}
        JSONArray dataList = JSONArray.parseArray(data);
        JSONObject dataObj = new JSONObject();
        for(int i=0;i<dataList.size();i++){
            JSONObject app_data = new JSONObject();
            JSONObject jsonObject = dataList.getJSONObject(i);
            app_data.put("type", jsonObject.getString("type"));
            app_data.put("value", jsonObject.getString("value"));
            dataObj.put(jsonObject.getString("paramName"), app_data);
        }
        if(dataObj.containsKey("appId")){
            String appValue = dataObj.getJSONObject("appId").getString("value");
            if(appValue != null && !appValue.equals("")){
                // 根据域名标识来获取域名
                Application application = applicationMapper.getApplicationById(appValue);
                if(application != null) {
                    dataObj.getJSONObject("appId").put("value", application.getAppId());
                }
            }
        }
        return dataObj;
    }

    private JSONObject getAppElement(String elements){
        JSONArray element_list = JSONArray.parseArray(elements);
        JSONObject elementObj = new JSONObject();
        for(int i=0;i<element_list.size();i++){
            JSONObject element = new JSONObject();
            JSONObject jsonObject = element_list.getJSONObject(i);
            if(jsonObject.getString("type").equals("id")) {
                ControlDTO controlDTO = controlMapper.getControlById(element.getString("id"));
                element.put("by", controlDTO.getBy());
                element.put("expression", controlDTO.getExpression());
                element.put("target", controlDTO.getModuleName() + " / " + controlDTO.getName());
            }else{
                element.put("by", jsonObject.getString("by"));
                element.put("expression", jsonObject.getString("expression"));
                element.put("target", element.getString("moduleName") + " / " + element.getString("name"));
            }
            elementObj.put(jsonObject.getString("paramName"), element);
        }
        return elementObj;
    }

    private TestCaseApiResponse getApiTestCaseJson(String environmentId, Integer sourceType, TaskTestCaseResponse taskTestCase) {
        TestCaseApiResponse testCaseApiResponse = new TestCaseApiResponse();
        CaseDTO caseDTO;
        if(sourceType.equals(SourceType.TEMP.getValue())){
            String debugData = debugDataMapper.getDebugData(taskTestCase.getCaseId());
            caseDTO = JSONObject.parseObject(debugData, CaseDTO.class);
        }else {
            caseDTO = caseMapper.getCaseById(taskTestCase.getCaseId());
            caseDTO.setCaseApis(caseApiMapper.getCaseApiList(taskTestCase.getCaseId()));
        }
        testCaseApiResponse.setCaseId(caseDTO.getId());
        testCaseApiResponse.setCaseName(caseDTO.getName());
        testCaseApiResponse.setComment(caseDTO.getDescription());
        JSONObject commonParam = JSONObject.parseObject(caseDTO.getCommonParam());

        testCaseApiResponse.setFunctions(getApiFunction(commonParam.getJSONArray("functions")));
        testCaseApiResponse.setParams(getCaseParam(commonParam.getJSONArray("params")));
        List<CaseApiDTO> caseApis = caseDTO.getCaseApis();
        List<TestCaseApiDataResponse> apiDataList = new ArrayList<>();
        for(CaseApiDTO caseApi :caseApis){
            TestCaseApiDataResponse apiData = new TestCaseApiDataResponse();
            apiData.setApiId(caseApi.getApiId());
            apiData.setApiName(caseApi.getApiName());
            apiData.setUrl(getUrlBySign(environmentId, caseApi.getApiServerSign(), caseApi.getApiPath()));
            apiData.setPath(caseApi.getApiPath());
            apiData.setMethod(caseApi.getApiMethod());
            apiData.setProtocol(caseApi.getApiProtocol());
            apiData.setBody(JSONObject.parseObject(caseApi.getBody()));
            apiData.setHeaders(getApiHeader(commonParam.getString("header"),caseApi.getHeader()));
            apiData.setProxies(getApiProxy(commonParam.getString("proxy")));
            apiData.setQuery(getApiQuery(caseApi.getQuery()));
            // 组装rest
            apiData.setRest(getApiRest(caseApi.getRest()));
            // 组装relation assertion
            apiData.setRelations(JSONArray.parseArray(caseApi.getRelation()));
            apiData.setAssertions(JSONArray.parseArray(caseApi.getAssertion()));
            // 组装controller
            apiData.setController(getApiController(caseApi.getController()));
            apiDataList.add(apiData);
        }
        testCaseApiResponse.setApiList(apiDataList);

        return testCaseApiResponse;

    }

    // 组装web数据
    private TestCaseWebResponse getWebTestCaseJson(String environmentId, Integer sourceType, TaskTestCaseResponse taskTestCase) {
        TestCaseWebResponse testCaseWebResponse = new TestCaseWebResponse();
        CaseDTO caseDTO;
        if(sourceType.equals(SourceType.TEMP.getValue())){
            String debugData = debugDataMapper.getDebugData(taskTestCase.getCaseId());
            caseDTO = JSONObject.parseObject(debugData, CaseDTO.class);
        } else {
            caseDTO = caseMapper.getCaseById(taskTestCase.getCaseId());
            QueryRequest request = new QueryRequest();
            caseDTO.setCaseWebs(caseWebMapper.getCaseWebList(taskTestCase.getCaseId()));
        }
        testCaseWebResponse.setCaseId(caseDTO.getId());
        testCaseWebResponse.setCaseName(caseDTO.getName());
        testCaseWebResponse.setComment(caseDTO.getDescription());
        JSONObject commonParam = JSONObject.parseObject(caseDTO.getCommonParam());
        testCaseWebResponse.setFunctions(this.getCaseFunctions(commonParam.getJSONArray("functions")));
        testCaseWebResponse.setParams(this.getCaseParams(commonParam.getJSONArray("params")));
        testCaseWebResponse.setStartDriver(commonParam.getBoolean("startDriver"));
        testCaseWebResponse.setCloseDriver(commonParam.getBoolean("closeDriver"));
        List<CaseWebDTO> caseWebs = caseDTO.getCaseWebs();
        List<TestCaseWebDataResponse> optList = new ArrayList<>();
        for (CaseWebDTO caseWebDTO:caseWebs){
            TestCaseWebDataResponse optData = new TestCaseWebDataResponse();
            Operation operation = operationMapper.getOperationDetail(caseWebDTO.getOperationId());
            optData.setOperationType(operation.getType());  // 操作类型
            optData.setOperationId(caseWebDTO.getOperationId());
            if(operation.getFrom().equals("custom")){
                optData.setOperationName("自定义");
                optData.setOperationCode(operation.getCode());
            }else {
                optData.setOperationName(operation.getName());
                optData.setOperationCode(null);
            }
            optData.setOperationTrans(operation.getName());
            optData.setOperationElement(this.getWebElement(caseWebDTO.getElements()));
            optData.setOperationData(this.getWebData(caseWebDTO.getData(), environmentId));
            optList.add(optData);
        }
        testCaseWebResponse.setOptList(optList);
        return testCaseWebResponse;

    }
    public JSONObject getWebElement(String elementText){
        // 获取web操作的元素 {元素名称:{by:定位方式,expression:表达式}}
        JSONArray elements = JSONArray.parseArray(elementText);
        JSONObject elementObj = new JSONObject();
        if(elements == null){
            return elementObj;
        }
        for(int i=0;i<elements.size();i++){
            JSONObject element = elements.getJSONObject(i);
            JSONObject elementData = new JSONObject();
            // 获取最新元素
            ElementDTO elementDTO = elementMapper.getElementById(element.getString("id"));
            if(elementDTO != null) {
                elementData.put("by", elementDTO.getBy());
                elementData.put("expression", elementDTO.getExpression());
                elementData.put("target", elementDTO.getModuleName() + " / " + elementDTO.getName());
            }else {
                elementData.put("by", element.getString("by"));
                elementData.put("expression", element.getString("expression"));
                elementData.put("target", element.getString("moduleName") + " / " + element.getString("name"));
            }
            elementObj.put(element.getString("paramName"), elementData);
        }
        return elementObj;
    }

    public JSONArray getCaseFunctions(JSONArray functions){
        // 获取用例所需要的自定义函数
        JSONArray functionList = new JSONArray();
        for(int i=0; i<functions.size();i++){
            JSONObject functionObj = new JSONObject();
            String functionId = functions.getString(i);
            Function function = functionMapper.getFunctionDetail(functionId);
            functionObj.put("name", function.getName());
            functionObj.put("code", function.getCode());
            JSONArray params = JSONArray.parseArray(function.getParams());
            JSONObject paramObj = new JSONObject();
            paramObj.put("names", new JSONArray());
            paramObj.put("types", new JSONArray());
            for(int j=0; j<params.size(); j++){
                JSONObject param = params.getJSONObject(j);
                paramObj.getJSONArray("names").add(param.getString("paramName"));
                paramObj.getJSONArray("types").add(param.getString("type"));
            }
            functionObj.put("params", paramObj);
            functionList.add(functionObj);
        }
        // {name:"",code:"",params: {names:[],types:[]}}
        return functionList;
    }

    public JSONObject getCaseParams(JSONArray params){
        // 获取用例所需要的公参
        JSONObject paramObj = new JSONObject();
        for(int i=0; i<params.size();i++){
            String paramId = params.getString(i);
            Params paramData = commonParamMapper.getParamDetail(paramId);
            JSONObject param = new JSONObject();
            param.put("type", paramData.getDataType());
            param.put("value", paramData.getParamData());
            paramObj.put(paramData.getName(), param);
        }
        // {name:{type:"",value:""}}
        return paramObj;
    }


    public JSONObject getWebData(String dataText, String environmentId){
        // 获取web操作需要的数据 {数据名称: {type:"",value:""}}
        JSONArray dataList = JSONArray.parseArray(dataText);
        JSONObject dataObj = new JSONObject();
        if(dataList == null){
            return dataObj;
        }
        for(int i=0;i<dataList.size();i++){
            JSONObject data = dataList.getJSONObject(i);
            JSONObject dataValue = new JSONObject();
            dataValue.put("type", data.getString("type"));
            dataValue.put("value", data.getString("value"));
            dataObj.put(data.getString("paramName"), dataValue);
        }
        // 对domain以及path字段处理
        if(dataObj.containsKey("domain")){
            String domainValue = dataObj.getJSONObject("domain").getString("value");
            if(domainValue != null && !domainValue.equals("")){
                // 根据域名标识来获取域名
                Domain domain = domainMapper.getDomainByName(environmentId, domainValue);
                if(domain != null) {
                    dataObj.getJSONObject("domain").put("value", domain.getDomainValue());
                }
            }else {  // 根据path来获取域名
                if(dataObj.containsKey("path")){
                    String path = dataObj.getJSONObject("path").getString("value");
                    List<Domain> domainList = domainMapper.getDomainList(environmentId);
                    for(Domain domain: domainList){
                        String domainKey = domain.getDomainKey();
                        if(path.startsWith(domainKey)){
                            dataObj.getJSONObject("domain").put("value", domain.getDomainValue());
                            break;
                        }
                    }
                }
            }
        }
        return dataObj;
    }


    private JSONObject getApiQuery(String query){
        JSONArray queryList = JSONArray.parseArray(query);
        JSONObject queryObj = new JSONObject();
        if(query==null){
            return queryObj;
        }
        for (int i = 0; i < queryList.size(); i++) {
            JSONObject queryData = queryList.getJSONObject(i);
            queryObj.put(queryData.getString("name"),queryData.getString("value"));
        }
        return queryObj;
    }
    public JSONObject getApiController(String controllerText){
        // 将controller列表转化为一个json{name:value}
        JSONArray controller = JSONArray.parseArray(controllerText);
        JSONObject controllerObj = new JSONObject();
        if(controller == null){
            return controllerObj;
        }
        for(int i =0; i<controller.size(); i++) {
            JSONObject controllerData = controller.getJSONObject(i);
            controllerObj.put(controllerData.getString("name"), controllerData.getString("value"));
        }
        return controllerObj;
    }

    public JSONObject getApiRest(String restText){
        // 将rest列表转化为一个json{name:value}
        JSONArray rest = JSONArray.parseArray(restText);
        JSONObject restObj = new JSONObject();
        if(rest == null){
            return restObj;
        }
        for(int i =0; i<rest.size(); i++) {
            JSONObject restData = rest.getJSONObject(i);
            restObj.put(restData.getString("name"), restData.getString("value"));
        }
        return restObj;
    }

    private JSONObject getApiProxy(String proxyId){
        Params paramData = paramsMapper.getParamDetail(proxyId);
        if(paramData == null){
            return new JSONObject();
        }
        return JSONObject.parseObject(paramData.getParamData());
    }


    private JSONObject getApiHeader(String commonHeaderId, String apiHeader){
        Params paramData = paramsMapper.getParamDetail(commonHeaderId);
        JSONArray headerList = JSONArray.parseArray(apiHeader);
        try{
            JSONObject header = new JSONObject();
            if(paramData != null){
                header = JSONObject.parseObject(paramData.getParamData());  // 解析公参里的header
            }
            if(headerList == null){
                return header;  // 如果接口并没有维护请求头 则直接使用全部的公共请求头
            }

        for(int i=0;i<headerList.size();i++) {
            JSONObject api_header = headerList.getJSONObject(i);
            String headerKey = api_header.getString("key");
            String headerValue = api_header.getString("value");
            for (String item : header.keySet()) {
                // 用例中的同名header key 替换公参中的 key不区分大小写 如content-type
                if (item.equalsIgnoreCase(headerKey)) {
                    // 如果公参里有接口中维护的请求头配置 则需要移除 并使用接口中的配置
                    header.remove(item);
                    break;
                }
            }
            header.put(headerKey, headerValue);
        }

        return header;
    }catch (Exception e){
        throw new DemoException("请求头解析失败");
    }
    }

    private JSONArray getApiFunction(JSONArray functions){
        JSONArray functionList = new JSONArray();
        for(int i=0;i<functions.size();i++){
            String functionId = functions.getString(i);
            Function function = functionMapper.getFunctionDetail(functionId);
            JSONObject functionJson = new JSONObject();
            functionJson.put("name",function.getName());
            functionJson.put("code",function.getCode());
            JSONObject paramObj = new JSONObject();
            JSONArray params = JSONArray.parseArray(function.getParams());
            paramObj.put("names", new JSONArray());
            paramObj.put("types", new JSONArray());
            for(int j=0; j<params.size(); j++){
                JSONObject param = params.getJSONObject(j);
                paramObj.getJSONArray("names").add(param.getString("paramName"));
                paramObj.getJSONArray("types").add(param.getString("type"));
            }
            functionJson.put("params",paramObj);
            functionList.add(functionJson);

        }
        return functionList;
    }

    private JSONObject getCaseParam(JSONArray params){
        JSONObject paramObj = new JSONObject();
        for(int i=0; i<params.size(); i++){
            String paramId = params.getString(i);
            Params paramData = paramsMapper.getParamDetail(paramId);
            JSONObject param = new JSONObject();
            param.put("type",paramData.getDataType());
            param.put("value", paramData.getParamData());
            paramObj.put(paramData.getName(),param);
        }
        return paramObj;
    }

    public String getUrlBySign( String environmentId, String serverSign, String path){
        // 匹配环境下的域名 匹配不到则为null
        String url = null;
        if(serverSign != null && !serverSign.equals("")){
            Domain domain = domainMapper.getDomainByName(environmentId, serverSign);
            if(domain != null) {
                url = domain.getDomainValue();
            }
        }else {
            List<Domain> domainList = domainMapper.getDomainList(environmentId);
            for(Domain domain: domainList){
                String domainKey = domain.getDomainKey();
                if(path.startsWith(domainKey)){ // 当路由是以匹配标识为开头时 默认使用该标识下的域名
                    url = domain.getDomainValue();
                    break;
                }
            }
        }
        return url;
    }




}
