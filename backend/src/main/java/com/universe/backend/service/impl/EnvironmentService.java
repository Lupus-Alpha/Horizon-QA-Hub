package com.universe.backend.service.impl;


import com.universe.backend.common.exception.DuplicateException;
import com.universe.backend.database.domain.Environment;
import com.universe.backend.database.mapper.EnvironmentMapper;
import com.universe.backend.request.QueryRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EnvironmentService {
    @Resource
    private EnvironmentMapper environmentMapper;

    public String saveEnvironment(Environment environment, HttpServletRequest request){
        Environment old = environmentMapper.getEnvironmentByName(environment.getProjectId(), environment.getName());
        if(old != null && !Objects.equals(old.getId(), environment.getId())){
            throw new DuplicateException("当前项目已有重名环境");
        }
        if(environment.getId() == null || environment.getId().equals("")){
            //新增环境
            environment.setId(UUID.randomUUID().toString());
            environment.setCreateTime(System.currentTimeMillis());
            environment.setUpdateTime(System.currentTimeMillis());
            environment.setCreateUser((String) request.getSession().getAttribute("account"));
            environment.setUpdateUser((String) request.getSession().getAttribute("account"));
        }else{
            // 更新环境
            environment.setUpdateTime(System.currentTimeMillis());
            environment.setUpdateUser((String) request.getSession().getAttribute("account"));
        }
        environmentMapper.saveEnvironment(environment);
        return environment.getId();
    }

    public void deleteEnvironment(String id) {
        environmentMapper.deleteEnvironment(id);
    }

    public Environment getEnvironmentDetail(String environmentId){
        return environmentMapper.getEnvironmentDetail(environmentId);
    }

    public List<Environment> getEnvironmentList(QueryRequest request){
        if(request.getCondition() != null && !request.getCondition().equals("")){
            request.setCondition("%"+request.getCondition()+"%");
        }
        return environmentMapper.getEnvironmentList(request);
    }

    public List<Environment> getAllEnvironment(String projectId){
        return environmentMapper.getAllEnvironment(projectId);

    }


}
