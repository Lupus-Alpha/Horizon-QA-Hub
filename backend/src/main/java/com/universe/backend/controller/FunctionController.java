package com.universe.backend.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.Function;
import com.universe.backend.database.domain.TestFile;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.FunctionService;
import com.universe.backend.service.impl.TestFileService;
import com.universe.backend.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/auto_test/function")
public class FunctionController {
    @Autowired
    private FunctionService functionService;

    @PostMapping(value = "/add")
    public String save(@RequestBody Function function, HttpServletRequest request){
        return functionService.saveFunction(function, request);

    }
    @PostMapping(value = "/delete/{functionId}")
    public void delete(@PathVariable String functionId){
        functionService.delete(functionId);
    }

    @GetMapping("/detail/{functionId}")
    public Function getFunctionDetail(@PathVariable String functionId){
        return functionService.getFunctionDetail(functionId);
    }


    @PostMapping("/list/{pageNum}/{pageSize}")
    public PageDTO<List<Function>> getTestFunctionList(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                                                   @RequestBody QueryRequest queryRequest){
        Page<Object> page = PageHelper.startPage(pageNum, pageSize, true);
        return PageUtils.setPageInfo(page, functionService.getFunctionList(queryRequest));
    }

    @GetMapping("/custom/all/{projectId}")
    public List<Function> getAllCustomFunction(@PathVariable String projectId){
        return functionService.getAllCustomFunction(projectId);
    }




}
