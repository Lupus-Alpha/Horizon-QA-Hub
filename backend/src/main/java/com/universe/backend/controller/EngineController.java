package com.universe.backend.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.Engine;
import com.universe.backend.database.domain.Function;
import com.universe.backend.dto.EngineDTO;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.EngineService;
import com.universe.backend.service.impl.FunctionService;
import com.universe.backend.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/auto_test/engine")
public class EngineController {
    @Autowired
    private EngineService engineService;

    @PostMapping(value = "/register")
    public Engine register(@RequestBody Engine engine, HttpServletRequest request){
        return engineService.saveEngine(engine, request);

    }
    @RequestMapping(value = "/delete")
    public void delete(@RequestBody Engine engine){
        engineService.delete(engine);
    }
    @PostMapping("/list/{goPage}/{pageSize}")
    public PageDTO<List<Engine>> getEnvironmentList(@PathVariable int goPage, @PathVariable int pageSize,
                                                    @RequestBody QueryRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, engineService.getEngineList(request));
    }

    @GetMapping("/detail/{engineId}")
    public EngineDTO getEngineDetail(@PathVariable String engineId) {
        return engineService.getEngineDetail(engineId);
    }

    @GetMapping("/all/{projectId}")
    public List<Engine> getAllEngines(@PathVariable String projectId) {
        return engineService.getAllEngines(projectId);
    }



}
