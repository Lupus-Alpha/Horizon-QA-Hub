package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.Environment;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface EnvironmentMapper {
    Environment getEnvironmentByName(@Param("projectId") String projectId,@Param("name") String name);

    void saveEnvironment(Environment environment);

    void deleteEnvironment(String id);

    Environment getEnvironmentDetail(String environmentId);

    List<Environment> getEnvironmentList(QueryRequest queryRequest);

    List<Environment> getAllEnvironment(String projectId);
}
