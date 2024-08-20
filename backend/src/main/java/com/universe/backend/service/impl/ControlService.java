package com.universe.backend.service.impl;

import com.universe.backend.database.domain.Control;
import com.universe.backend.database.mapper.ControlMapper;
import com.universe.backend.dto.ControlDTO;
import com.universe.backend.request.QueryRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class ControlService {
    @Resource
    private ControlMapper controlMapper;

    public void saveControl(Control control) {
        if(control.getId().equals("") || control.getId() == null){ // 新增控件
            control.setId(UUID.randomUUID().toString());
            control.setCreateTime(System.currentTimeMillis());
            control.setUpdateTime(System.currentTimeMillis());
            control.setCreateUser(control.getUpdateUser());
            controlMapper.addControl(control);
        }else{ // 修改控件
            control.setUpdateTime(System.currentTimeMillis());
            controlMapper.updateControl(control);
        }
    }

    public void deleteControl(String id) {
        controlMapper.deleteControl(id);
    }

    public List<Control> getListModuleControls(String system, String moduleId){
        return controlMapper.getListModuleControls(system, moduleId);
    }


    public List<ControlDTO> getControlList(QueryRequest request){
        if(request.getCondition() != null && !request.getCondition().equals("")){
            request.setCondition("%"+request.getCondition()+"%");
        }
        return controlMapper.getControlList(request);
    }
}
