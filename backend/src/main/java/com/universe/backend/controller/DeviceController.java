package com.universe.backend.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.Device;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.DeviceService;
import com.universe.backend.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/auto_test/device")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @PostMapping("/list/{goPage}/{pageSize}")
    public PageDTO<List<Device>> getDevicePageList(@PathVariable int goPage, @PathVariable int pageSize,
                                                   @RequestBody QueryRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, deviceService.getDeviceList(request));
    }
    @GetMapping("/all/{system}/{projectId}")
    // 执行时选择设备时使用 system参数值: android、apple、total
    public List<Device> getDeviceListByProject(@PathVariable String projectId, @PathVariable String system) {
        return deviceService.getDeviceListByProjectId(projectId, system);
    }
}
