package com.universe.backend.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.Environment;
import com.universe.backend.database.domain.ServerSign;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.ServerSignService;
import com.universe.backend.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auto_test/server_sign")
public class ServerSignController {
    @Autowired
    private ServerSignService serverSignService;

    @PostMapping(value = "/add")
    public String save(@RequestBody ServerSign serverSign){
        return serverSignService.saveServerSign(serverSign);

    }
    @PostMapping("/delete/{serverSignId}")
    public void deleteServerSign(@PathVariable String serverSignId) {
        serverSignService.deleteServerSign(serverSignId);
    }

    @GetMapping("/all/{projectId}")
    public List<ServerSign> getServerSignList(@PathVariable String projectId) {
        return serverSignService.getAllServerSign(projectId);
    }

    @PostMapping("/list/{goPage}/{pageSize}")
    public PageDTO<List<ServerSign>> getServerSignPageList(@PathVariable int goPage, @PathVariable int pageSize,
                                                           @RequestBody QueryRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, serverSignService.getServerSignList(request));
    }
}
