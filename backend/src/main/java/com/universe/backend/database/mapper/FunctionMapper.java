package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.Function;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FunctionMapper {

    int save(Function record);

    Function getFunctionByName(@Param("projectId")String projectId, @Param("name")String name);

    List<Function> getFunctionList(QueryRequest request);

    void delete(String id);

    Function getFunctionDetail(String id);

    List<Function> getAllCustomFunction(String projectId);


}
