package com.universe.backend.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.Application;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.ApplicationService;
import com.universe.backend.utils.PageUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/auto_test/application")
public class applicationController {
    @Resource
    private ApplicationService applicationService;


    @PostMapping("/list/{goPage}/{pageSize}")
    public PageDTO<List<Application>> getApplicationPageList(@PathVariable int goPage, @PathVariable int pageSize,
                                                             @RequestBody QueryRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, applicationService.getApplicationList(request));
    }

    @GetMapping("/all/{system}/{projectId}")
    public List<Application> getAllApplication(@PathVariable String system, @PathVariable String projectId) {
        return applicationService.getAllApplication(projectId, system);
    }

    @PostMapping("/save")
    public void saveApplication(@RequestBody Application application) {
        applicationService.saveApplication(application);
    }
}
