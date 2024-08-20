package com.universe.backend.service.impl;

import com.universe.backend.common.exception.DemoException;
import com.universe.backend.common.exception.DuplicateException;
import com.universe.backend.database.mapper.ModuleMapper;
import com.universe.backend.dto.ModuleDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ModuleService {
    @Resource
    private ModuleMapper moduleMapper;

    public ModuleDTO save(ModuleDTO moduleDTO) {
        ModuleDTO old = moduleMapper.getModuleByParentAndName(moduleDTO.getModuleType(),
                moduleDTO.getName(), moduleDTO.getParentId(), moduleDTO.getProjectId());
        if(old != null){ //判断当前项目下这个类型下同一个父模块下是否有重名模块
            throw new DuplicateException("当前父模块下已有重名模块");
        }
        if(moduleDTO.getId() == null){
            moduleDTO.setId(UUID.randomUUID().toString());
            moduleDTO.setCreateTime(System.currentTimeMillis());
            moduleDTO.setUpdateTime(System.currentTimeMillis());
        }else{
            moduleDTO.setUpdateTime(System.currentTimeMillis());
        }
        moduleMapper.saveModule(moduleDTO);
        moduleDTO.setLabel(moduleDTO.getName());
        return moduleDTO;
    }

    public void delete(ModuleDTO moduleDTO) {
        Integer count = moduleMapper.getModuleDataById(moduleDTO.getModuleType(), moduleDTO.getId());

        if(count > 0 & moduleDTO.getChildren() !=null){ //这个模块下有数据或者有子模块就无法删除
            throw new DemoException("当前模块下已有相关数据，无法删除！");
        }
        moduleMapper.deleteModule(moduleDTO.getId());
    }

    public List<ModuleDTO> getModuleList(String moduleType, String projectId){
        List<ModuleDTO> fina = new ArrayList<>();
        List<ModuleDTO> apiModuleDTOS = moduleMapper.getModuleList(moduleType, projectId);
        for(ModuleDTO apiModuleDTO: apiModuleDTOS){
            if(apiModuleDTO.getParentId().equals("0")){
                apiModuleDTO.setChildren(this.nodeList(apiModuleDTOS, apiModuleDTO.getId()));
                fina.add(apiModuleDTO);
            }
        }
        return fina;
    }

    private List<ModuleDTO> nodeList(List<ModuleDTO> ModuleDTOs,String id){
        List<ModuleDTO> childModule = new ArrayList<>();
        for(ModuleDTO moduleDTO: ModuleDTOs){
            if(Objects.equals(moduleDTO.getParentId(), id)){
                childModule.add(moduleDTO);
                moduleDTO.setChildren(nodeList(ModuleDTOs,moduleDTO.getId()));
            }

            }

        return childModule;
    }


}
