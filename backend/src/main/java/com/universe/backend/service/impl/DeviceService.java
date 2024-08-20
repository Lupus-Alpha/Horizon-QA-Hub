package com.universe.backend.service.impl;

import com.universe.backend.database.domain.Device;
import com.universe.backend.database.mapper.DeviceMapper;
import com.universe.backend.request.QueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DeviceService {
    @Resource
    private DeviceMapper deviceMapper;

    public List<Device> getDeviceList(QueryRequest request) {
        if(request.getCondition() != null && !request.getCondition().equals("")){
            request.setCondition("%"+request.getCondition()+"%");
        }
        return deviceMapper.getDeviceList(request);
    }

    public List<Device> getDeviceListByProjectId(String projectId, String system) {
        return deviceMapper.getDeviceListByProjectId(projectId, system);
    }
}
