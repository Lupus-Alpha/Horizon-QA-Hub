package com.universe.backend.database.mapper;


import com.universe.backend.database.domain.Operation;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OperationMapper {
    void saveOperation(Operation operation);

    void delete(String id);

    Operation getOperationDetail(String id);

    Operation getOperationByName(Map<String, Object> parameters);

    List<Operation> getOperationList(QueryRequest request);

    List<Operation> getGroupOperationList(@Param("uiType")String uiType,@Param("system") String system,@Param("projectId") String projectId,@Param("operationType") String operationType);
}
