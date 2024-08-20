package com.universe.backend.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.Params;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.ParamsService;
import com.universe.backend.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/auto_test/params")
public class ParamsController {
    @Autowired
    private ParamsService paramsService;

    @PostMapping(value = "/add")
    public String save(@RequestBody Params params, HttpServletRequest request){
        return paramsService.saveParams(params, request);

    }
    @PostMapping(value = "/delete/{paramsId}")
    public void delete(@PathVariable String paramsId){
        paramsService.delete(paramsId);
    }

    @GetMapping("/detail/{functionId}")
    public Params getFunctionDetail(@PathVariable String functionId){
        return paramsService.getFunctionDetail(functionId);
    }

    @PostMapping("/list/{pageNum}/{pageSize}")
    public PageDTO<List<Params>> getTestFunctionList(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                                                       @RequestBody QueryRequest queryRequest){
        Page<Object> page = PageHelper.startPage(pageNum, pageSize, true);
        return PageUtils.setPageInfo(page, paramsService.getFunctionList(queryRequest));
    }

    @GetMapping("/all/{type}/{projectId}")
    public List<Params> getAllParams(@PathVariable String type, @PathVariable String projectId){
        return paramsService.getAllParams(type, projectId);
    }


}
