package com.universe.backend.service.impl;

import com.universe.backend.common.exception.DuplicateException;
import com.universe.backend.database.domain.Application;
import com.universe.backend.database.mapper.ApplicationMapper;
import com.universe.backend.request.QueryRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
public class ApplicationService {
    @Resource
    private ApplicationMapper applicationMapper;

    public void saveApplication(Application application) {
        Application old = applicationMapper.getApplicationByName(application.getProjectId(), application.getName());
        if(old != null && !Objects.equals(old.getId(), application.getId())){
            throw new DuplicateException("当前项目已有重名应用");
        }
        if(application.getId() == null || application.getId().equals("")){
            //新增版本
            application.setId(UUID.randomUUID().toString());
            application.setCreateTime(System.currentTimeMillis());
            application.setUpdateTime(System.currentTimeMillis());
        }else{
            // 更新版本
            application.setUpdateTime(System.currentTimeMillis());
        }
        applicationMapper.saveApplication(application);
    }

    public List<Application> getApplicationList(QueryRequest request) {
        if(request.getCondition() != null && !request.getCondition().equals("")){
            request.setCondition("%"+request.getCondition()+"%");
        }
        return applicationMapper.getApplicationList(request);
    }

    public List<Application> getAllApplication(String projectId,String system){
        return applicationMapper.getAllApplication(projectId, system.toUpperCase(Locale.ROOT));
    }
}
