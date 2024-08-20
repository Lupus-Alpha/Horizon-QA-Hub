package com.universe.backend.database.mapper;


import com.universe.backend.database.domain.Engine;
import com.universe.backend.dto.EngineDTO;
import com.universe.backend.request.EngineRequest;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EngineMapper {
    Engine getEngineByName(String projectId, String name);

    void saveEngine(Engine engine);

    void deleteEngine(String id);

    List<Engine> getEngineList(QueryRequest request);

    EngineDTO getEngineDetail(String id);

    void updateHeartbeat(@Param("time")Long time,@Param("id") String id);

    Engine getEngineById(String id);

    void updateStatus(@Param("id")String id, @Param("status")String status);

    void updateOfflineEngine(Long minLastHeartbeatTime);

    List<Engine> getAllEngines(String projectId);
}
