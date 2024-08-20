package com.universe.backend.controller;

import com.universe.backend.dto.ModuleDTO;
import com.universe.backend.service.impl.ModuleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/auto_test/module")
public class MoudleController {
    @Resource
    private ModuleService moduleService;

    @PostMapping("/save")
    public ModuleDTO save(@RequestBody ModuleDTO moduleDTO, HttpServletRequest request) {
        String user = request.getSession().getAttribute("account").toString();
        moduleDTO.setUpdateUser(user);
        moduleDTO.setCreateUser(user);
        return moduleService.save(moduleDTO);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody ModuleDTO moduleDTO) {
        moduleService.delete(moduleDTO);
    }

    @GetMapping("/list/{moduleType}/{projectId}")
    public List<ModuleDTO> getModuleList(@PathVariable String moduleType, @PathVariable String projectId) {
        return moduleService.getModuleList(moduleType, projectId);
    }


}
