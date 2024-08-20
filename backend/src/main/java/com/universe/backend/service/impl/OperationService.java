package com.universe.backend.service.impl;

import com.universe.backend.common.constant.OperationType;
import com.universe.backend.common.exception.DuplicateException;
import com.universe.backend.database.domain.Function;
import com.universe.backend.database.domain.Operation;
import com.universe.backend.database.mapper.OperationMapper;
import com.universe.backend.dto.OperationGroupDTO;
import com.universe.backend.request.QueryRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class OperationService {
    @Resource
    private OperationMapper operationMapper;

    public String saveOperation(Operation operation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("projectId", operation.getProjectId());
        parameters.put("name", operation.getName());
        parameters.put("system", operation.getSystem());
        Operation old = operationMapper.getOperationByName(parameters);
        if(old != null && !old.getId().equals(operation.getId())){
            throw new DuplicateException("本项目已有该操作名称");
        }
        if(operation.getId() !=null && !operation.getId().equals("")){
            operation.setUpdateTime(System.currentTimeMillis());
        }else {
            operation.setId(UUID.randomUUID().toString());
            operation.setUpdateUser(operation.getCreateUser());
            operation.setCreateTime(System.currentTimeMillis());
            operation.setUpdateTime(System.currentTimeMillis());
        }
        operationMapper.saveOperation(operation);
        return operation.getId();
    }
    public void delete(String id){
        operationMapper.delete(id);
    }

    public Operation getOperationDetail(String id){
        return operationMapper.getOperationDetail(id);
    }

    public List<Operation> getOperationList(QueryRequest request){
        if(request.getCondition() != null && !request.getCondition().equals("")){
            request.setCondition("%"+request.getCondition()+"%");
        }
        return operationMapper.getOperationList(request);
    }

    public List<OperationGroupDTO> getGroupOperationList(String uiType,String system,String projectId){
        List<OperationGroupDTO>  operationGroupDTOList = new ArrayList<>();
        List<String> oerationType = OperationType.enumList(uiType);
        for(String operationType : oerationType){
            OperationGroupDTO operationGroup = new OperationGroupDTO();
            operationGroup.setId(operationType);
            operationGroup.setName(OperationType.valueOf(operationType.toUpperCase(Locale.ROOT)).toLabel());
            List<Operation> operationList = operationMapper.getGroupOperationList(projectId,uiType,system,operationType);
            operationGroup.setOperationList(operationList);
            operationGroupDTOList.add(operationGroup);
        }
        return operationGroupDTOList;

    }

}
