package com.universe.backend.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.Control;
import com.universe.backend.dto.ControlDTO;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.ControlService;
import com.universe.backend.utils.PageUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/auto_test/control")
public class ControlController {
    @Resource
    private ControlService controlService;

    @PostMapping("/save")
    public void saveControl(@RequestBody Control control, HttpServletRequest request) {
        String user = request.getSession().getAttribute("account").toString();
        control.setUpdateUser(user);
        controlService.saveControl(control);
    }

    @PostMapping("/delete/{controlId}")
    public void deleteControl(@PathVariable String controlId) {
        controlService.deleteControl(controlId);
    }

    @GetMapping("/list/module/{system}/{moduleId}")
    public List<Control> getListModuleControls(@PathVariable String system, @PathVariable String moduleId) {
        return controlService.getListModuleControls(system, moduleId);
    }

    @PostMapping("/list/{goPage}/{pageSize}")
    public PageDTO<List<ControlDTO>> getControlList(@PathVariable int goPage, @PathVariable int pageSize,
                                                    @RequestBody QueryRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, controlService.getControlList(request));
    }
}
