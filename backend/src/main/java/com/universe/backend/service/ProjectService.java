package com.universe.backend.service;

import com.universe.backend.database.domain.Project;
import com.universe.backend.request.QueryRequest;

import java.util.List;

public interface ProjectService {

    public List<Project> getProjectList(QueryRequest queryRequest);

    public void saveProject(Project project);


}
