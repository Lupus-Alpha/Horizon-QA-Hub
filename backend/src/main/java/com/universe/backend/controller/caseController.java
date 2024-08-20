package com.universe.backend.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.Case;
import com.universe.backend.dto.CaseDTO;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.CaseService;
import com.universe.backend.utils.PageUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/auto_test/case")
public class caseController {
    @Resource
    private CaseService caseService;

    @PostMapping("/save")
    public void saveCase(@RequestBody CaseDTO caseDTO, HttpServletRequest request) {
        String user = request.getSession().getAttribute("account").toString();
        caseDTO.setUpdateUser(user);
        caseService.saveCase(caseDTO);
    }

    @PostMapping("/list/{page_num}/{page_size}")
    public PageDTO<List<CaseDTO>> getCaseList(@PathVariable int page_num, @PathVariable int page_size, @RequestBody QueryRequest queryRequest) {
        Page<Object> page = PageHelper.startPage(page_num, page_size, true);
        return PageUtils.setPageInfo(page,caseService.getCaseList(queryRequest));
    }

    @GetMapping("/detail/{caseType}/{caseId}")
    public CaseDTO getCaseDetail(@PathVariable String caseType, @PathVariable String caseId){
        return caseService.getCaseDetail(caseType, caseId);
    }
}
