package com.universe.backend.service.impl;

import com.universe.backend.common.exception.DuplicateException;
import com.universe.backend.database.domain.Function;
import com.universe.backend.database.mapper.FunctionMapper;
import com.universe.backend.request.QueryRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
public class FunctionService {
    @Resource
    private FunctionMapper functionMapper;

    public String saveFunction(Function function, HttpServletRequest request) {
        Function old = functionMapper.getFunctionByName(function.getProjectId(), function.getName());
        if (old != null) {
            throw new DuplicateException("本项目已有该函数名称");
        }
        if(function.getId() !=null && !function.getId().equals("")) {
            function.setUpdateTime(System.currentTimeMillis());
        } else {
            function.setId(UUID.randomUUID().toString());
            function.setCreateUser((String) request.getSession().getAttribute("account"));
            function.setCreateTime(System.currentTimeMillis());
            function.setUpdateTime(System.currentTimeMillis());
            function.setUpdateUser((String) request.getSession().getAttribute("account"));
        }

        functionMapper.save(function);
        return function.getId();
    }

    public void delete(String id){
        functionMapper.delete(id);
    }

    public List<Function> getFunctionList(QueryRequest queryRequest) {
        if (queryRequest.getCondition() != null && !queryRequest.getCondition().equals("")) {
            queryRequest.setCondition("%" + queryRequest.getCondition() + "%");
        }
        return functionMapper.getFunctionList(queryRequest);
    }

    public Function getFunctionDetail(String id) {
        return functionMapper.getFunctionDetail(id);
    }

    public List<Function> getAllCustomFunction(String projectId) {
        return functionMapper.getAllCustomFunction(projectId);
    }
}
