package com.universe.backend.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.dto.PlanDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.PlanService;
import com.universe.backend.utils.PageUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("auto_test/plan")
public class PlanController {
    @Resource
    private PlanService planService;

    @PostMapping("/save")
    public void savePlan(@RequestBody PlanDTO planDTO, HttpServletRequest request) {
        String user = request.getSession().getAttribute("account").toString();
        planDTO.setUpdateUser(user);
        planService.savePlan(planDTO);
    }

    @PostMapping("/delete/{planId}")
    public void deletePlan(@PathVariable String planId) {
        planService.deletePlan(planId);
    }

    @PostMapping("/list/{goPage}/{pageSize}")
    public PageDTO<List<PlanDTO>> getPlanList(@PathVariable int goPage, @PathVariable int pageSize,
                                              @RequestBody QueryRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, planService.getPlanList(request));
    }

}
