package com.universe.backend.service.impl;

import com.universe.backend.common.exception.DuplicateException;
import com.universe.backend.database.domain.Api;
import com.universe.backend.database.mapper.ApiMapper;
import com.universe.backend.request.QueryRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class ApiService {
    @Resource
    private ApiMapper apiMapper;

    public void saveApi(Api api) {
        if(api.getId().equals("") || api.getId() == null) {
            Api old_api = apiMapper.getApiByName(api.getName());
            if (old_api != null) {
                throw new DuplicateException("接口名称已存在");
            }
            api.setCreateTime(System.currentTimeMillis());
            api.setUpdateTime(System.currentTimeMillis());
            api.setCreateUser(api.getUpdateUser());
            api.setId(UUID.randomUUID().toString());
            apiMapper.saveApi(api);
        } else {
            api.setUpdateTime(System.currentTimeMillis());
            apiMapper.updateApi(api);
        }

    }
    public void deleteApi(String apiId) {
        apiMapper.deleteApi(apiId);
    }

    public Api getApiDetail(String apiId){
        return apiMapper.getApiById(apiId);
    }

    public List<Api> getApiList(QueryRequest request){
        return apiMapper.getApiList(request);
    }


}
