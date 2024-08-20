package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.Application;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApplicationMapper {
    List<Application> getApplicationList(QueryRequest request);

    void saveApplication(Application application);

    Application getApplicationByName(@Param("projectId")String projectId,@Param("name") String name);

    Application getApplicationById(@Param("id") String id);

    List<Application> getAllApplication(@Param("projectId")String projectId,@Param("system") String system);

}
