package com.universe.backend.service.impl;

import com.universe.backend.common.exception.DuplicateException;
import com.universe.backend.database.domain.ServerSign;
import com.universe.backend.database.mapper.ServerSignMapper;
import com.universe.backend.request.QueryRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ServerSignService {
    @Resource
    private ServerSignMapper serverSignMapper;

    public String saveServerSign(ServerSign serverSign){
        ServerSign old = serverSignMapper.getServerSignByName(serverSign.getProjectId(), serverSign.getName());
        if(old != null && !Objects.equals(old.getId(), serverSign.getId())){
            throw new DuplicateException("当前项目已有重名服务标识");
        }
        if(serverSign.getId() == null || serverSign.getId().equals("")){
            //新增域名标识
            serverSign.setId(UUID.randomUUID().toString());
            serverSign.setCreateTime(System.currentTimeMillis());
            serverSign.setUpdateTime(System.currentTimeMillis());
        }else{
            // 更新版本
            serverSign.setUpdateTime(System.currentTimeMillis());
        }
        serverSignMapper.saveServerSign(serverSign);
        return serverSign.getId();
    }

    public void deleteServerSign(String id) {
        serverSignMapper.deleteServerSign(id);
    }

    public List<ServerSign> getAllServerSign(String projectId){
        return serverSignMapper.getAllServerSign(projectId);
    }

    public List<ServerSign> getServerSignList(QueryRequest request) {
        if(request.getCondition() != null && !request.getCondition().equals("")){
            request.setCondition("%"+request.getCondition()+"%");
        }
        return serverSignMapper.getServerSignList(request);
    }
}
