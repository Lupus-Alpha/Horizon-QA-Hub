package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.Project;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface projectMapper {
    String getProjectIdByUserId(String id);
    List<Project> getProjectList(QueryRequest queryRequest);
    void insertProject(Project project);
    Project getProjectByName(String name);
}
