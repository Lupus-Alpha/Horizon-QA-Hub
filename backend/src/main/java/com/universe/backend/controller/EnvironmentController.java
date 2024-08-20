package com.universe.backend.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.Domain;
import com.universe.backend.database.domain.Environment;
import com.universe.backend.database.domain.Function;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.DomainService;
import com.universe.backend.service.impl.EnvironmentService;
import com.universe.backend.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/auto_test/environment")
public class EnvironmentController {
    @Autowired
    private EnvironmentService environmentService;

    @Resource
    private DomainService domainService;

    @PostMapping(value = "/add")
    public String save(@RequestBody Environment environment, HttpServletRequest request){
        return environmentService.saveEnvironment(environment, request);

    }
    @PostMapping(value = "/delete/{functionId}")
    public void delete(@PathVariable String functionId){
        environmentService.deleteEnvironment(functionId);
    }

    @GetMapping("/detail/{environmentId}")
    public Environment getEnvironmentDetail(@PathVariable String environmentId){
        return environmentService.getEnvironmentDetail(environmentId);
    }


    @PostMapping("/list/{pageNum}/{pageSize}")
    public PageDTO<List<Environment>> getTestFunctionList(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                                                       @RequestBody QueryRequest queryRequest){
        Page<Object> page = PageHelper.startPage(pageNum, pageSize, true);
        return PageUtils.setPageInfo(page, environmentService.getEnvironmentList(queryRequest));
    }

    @GetMapping("/all/{projectId}")
    public List<Environment> getAllEnvironment(@PathVariable String projectId) {
        return environmentService.getAllEnvironment(projectId);
    }


    @PostMapping("/domain/save")
    public void saveDomain(@RequestBody Domain domain, HttpServletRequest request) {
        String user = request.getSession().getAttribute("account").toString();
        domain.setUpdateUser(user);
        domainService.saveDomain(domain);
    }

    @PostMapping("/delete/domain/{domainId}")
    public void deleteEnvironment(@PathVariable String domainId) {
        domainService.deleteDomain(domainId);
    }

    @GetMapping("/domain/list/{environmentId}")
    public List<Domain> getDomainList(@PathVariable String environmentId) {
        return domainService.getDomainList(environmentId);
    }

}
