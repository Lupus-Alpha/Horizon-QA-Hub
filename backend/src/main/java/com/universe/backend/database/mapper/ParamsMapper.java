package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.Params;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ParamsMapper {
    int save(Params record);

    Params getParamByName(@Param("projectId")String projectId, @Param("name")String name);

    List<Params> getParamList(QueryRequest request);

    void delete(String id);

    Params getParamDetail(String id);

    Params getParamById(String id);

    List<Params> getAllParams(@Param("groupName")String type, @Param("projectId")String projectId);
}
