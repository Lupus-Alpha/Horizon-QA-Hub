package com.universe.backend.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.Project;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.ProjectServiceIml;
import com.universe.backend.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auto_test/project")
public class ProjectController {
    @Autowired
    ProjectServiceIml projectService;

    @PostMapping("/list/{page_num}/{page_size}")
    public PageDTO<List<Project>> getProjectList(@PathVariable int page_num,@PathVariable int page_size, @RequestBody QueryRequest queryRequest){
        Page<Object> page= PageHelper.startPage(page_num,page_size,true);
        return PageUtils.setPageInfo(page, projectService.getProjectList(queryRequest));

    }
    @PostMapping("/add")
    public void addProject(@RequestBody Project project){
         projectService.saveProject(project);
    }

//    @PostMapping("/get")
//    public PageDTO<List<Project>> getProjectByName(@RequestBody String name){
//        Page<Object> page = PageHelper.startPage()
//        projectService.getProjectByName(name);
//    }
}
