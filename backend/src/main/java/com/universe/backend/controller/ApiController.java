package com.universe.backend.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.Api;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.ApiService;
import com.universe.backend.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/auto_test/api")
public class ApiController {
    @Autowired
    private ApiService apiService;

    @PostMapping("/save")
    public void saveApi(@RequestBody Api api, HttpServletRequest request) {
        String user = request.getSession().getAttribute("account").toString();
        api.setUpdateUser(user);

        apiService.saveApi(api);

    }

    @PostMapping("/delete/{apiId}")
    public void deleteApi(@PathVariable String apiId) {
        apiService.deleteApi(apiId);
    }

    @GetMapping("/detail/{apiId}")
    public Api getApiDetail(@PathVariable String apiId){
        return apiService.getApiDetail(apiId);
    }

    @PostMapping("/list/{goPage}/{pageSize}")
    public PageDTO<List<Api>> getApiList(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody QueryRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, apiService.getApiList(request));

    }




}

