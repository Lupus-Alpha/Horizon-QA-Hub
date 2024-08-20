package com.universe.backend.service.impl;

import com.universe.backend.common.exception.DuplicateException;
import com.universe.backend.database.domain.Function;
import com.universe.backend.database.domain.Params;
import com.universe.backend.database.mapper.FunctionMapper;
import com.universe.backend.database.mapper.ParamsMapper;
import com.universe.backend.request.QueryRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
public class ParamsService {
    @Resource
    private ParamsMapper paramsMapper;

    public String saveParams(Params params, HttpServletRequest request) {
        Params old = paramsMapper.getParamByName(params.getProjectId(), params.getName());
        if (old != null) {
            throw new DuplicateException("本项目已有该参数名称");
        }
        if(params.getId() !=null && !params.getId().equals("")) {
            params.setUpdateTime(System.currentTimeMillis());
        } else {
            params.setId(UUID.randomUUID().toString());
            params.setCreateUser((String) request.getSession().getAttribute("account"));
            params.setCreateTime(System.currentTimeMillis());
            params.setUpdateTime(System.currentTimeMillis());
            params.setUpdateUser((String) request.getSession().getAttribute("account"));
        }

        paramsMapper.save(params);
        return params.getId();
    }

    public void delete(String id){
        paramsMapper.delete(id);
    }

    public List<Params> getFunctionList(QueryRequest queryRequest) {
        if (queryRequest.getCondition() != null && !queryRequest.getCondition().equals("")) {
            queryRequest.setCondition("%" + queryRequest.getCondition() + "%");
        }
        return paramsMapper.getParamList(queryRequest);
    }

    public Params getFunctionDetail(String id) {
        return paramsMapper.getParamDetail(id);
    }

    public List<Params> getAllParams(String type,String projectId) {
        return paramsMapper.getAllParams(type,projectId);
    }
}
