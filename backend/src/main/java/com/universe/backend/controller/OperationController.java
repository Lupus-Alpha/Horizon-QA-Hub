package com.universe.backend.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.Operation;
import com.universe.backend.dto.OperationGroupDTO;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.OperationService;
import com.universe.backend.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/auto_test/operation")
public class OperationController {
    @Autowired
    private OperationService operationService;

    @PostMapping(value = "/add")
    public String save(@RequestBody Operation operation, HttpServletRequest request){
        operation.setCreateUser(request.getSession().getAttribute("account").toString());
        return operationService.saveOperation(operation);

    }
    @PostMapping(value = "/delete/{OperationId}")
    public void delete(@PathVariable String OperationId){
        operationService.delete(OperationId);
    }

    @GetMapping("/detail/{operationId}")
    public Operation getOperationDetail(@PathVariable String operationId){
        return operationService.getOperationDetail(operationId);
    }


    @PostMapping("/list/{pageNum}/{pageSize}")
    public PageDTO<List<Operation>> getTestOperationList(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                                                   @RequestBody QueryRequest queryRequest){
        Page<Object> page = PageHelper.startPage(pageNum, pageSize, true);
        return PageUtils.setPageInfo(page, operationService.getOperationList(queryRequest));
    }

    @GetMapping("/group/{uiType}/list/{projectId}")
    public List<OperationGroupDTO> getGroupOperationList(@PathVariable String uiType, @RequestParam String system, @PathVariable String projectId) {
        return operationService.getGroupOperationList(uiType, system, projectId);
    }




}
