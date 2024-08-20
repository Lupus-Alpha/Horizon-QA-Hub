package com.universe.backend.service.impl;


import com.universe.backend.common.constant.EngineStatus;
import com.universe.backend.common.exception.DuplicateException;
import com.universe.backend.database.domain.Engine;
import com.universe.backend.database.domain.Function;
import com.universe.backend.database.mapper.EngineMapper;
import com.universe.backend.dto.EngineDTO;
import com.universe.backend.request.QueryRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class EngineService {
    @Resource
    private EngineMapper engineMapper;

    public Engine saveEngine(Engine engine, HttpServletRequest request) {
        Engine old = engineMapper.getEngineByName(engine.getProjectId(), engine.getName());
        if(old != null){
            throw new DuplicateException("当前项目已有重名引擎");
        }
        engine.setId(UUID.randomUUID().toString().replace("-", ""));
        engine.setSecret(UUID.randomUUID().toString().replace("-",""));
        engine.setType("custom"); // 默认注册自定义引擎
        engine.setStatus(EngineStatus.OFFLINE.toString().toLowerCase(Locale.ROOT));  // 默认离线状态
        engine.setCreateTime(System.currentTimeMillis());
        engine.setUpdateTime(System.currentTimeMillis());
        engine.setCreateUser(engine.getUpdateUser());
        engineMapper.saveEngine(engine);
        return engine;
    }
    public void delete(Engine engine) {
        engineMapper.deleteEngine(engine.getId());
    }

    public List<Engine> getEngineList(QueryRequest queryRequest) {
        if (queryRequest.getCondition() != null && !queryRequest.getCondition().equals("")) {
            queryRequest.setCondition("%" + queryRequest.getCondition() + "%");
        }
        return engineMapper.getEngineList(queryRequest);
    }

    public EngineDTO getEngineDetail(String id) {
       EngineDTO engineDTO = engineMapper.getEngineDetail(id);
       engineDTO.setTasks(new ArrayList<>());
         return engineDTO;
    }

    public List<Engine> getAllEngines(String projectId) {
        return engineMapper.getAllEngines(projectId);
    }
}
