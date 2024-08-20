package com.universe.backend.database.mapper;


import com.universe.backend.database.domain.ServerSign;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ServerSignMapper {
    void saveServerSign(ServerSign serverSign);

    void deleteServerSign(String id);

    ServerSign getServerSignByName(@Param("projectId") String projectId,@Param("name") String name);

    List<ServerSign> getAllServerSign(String projectId);

    List<ServerSign> getServerSignList(QueryRequest request);
}
