package com.universe.backend.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.dto.ReportDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.ReportService;
import com.universe.backend.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auto_test/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @PostMapping("/list/{goPage}/{pageSize}")
    public PageDTO<List<ReportDTO>> getReportList(@PathVariable int goPage, @PathVariable int pageSize,
                                                  @RequestBody QueryRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, reportService.getReportList(request));
    }

    @GetMapping("/get/{reportId}")
    public ReportDTO getReport(@PathVariable String reportId) {
        return reportService.getReport(reportId);
    }
}
