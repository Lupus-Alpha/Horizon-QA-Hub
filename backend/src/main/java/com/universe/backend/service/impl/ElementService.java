package com.universe.backend.service.impl;

import com.universe.backend.database.domain.Element;
import com.universe.backend.database.mapper.ElementMapper;
import com.universe.backend.dto.ElementDTO;
import com.universe.backend.request.QueryRequest;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class ElementService {
    @Resource
    private ElementMapper elementMapper;

    public void saveElement(Element element) {
        if(element.getId().equals("") || element.getId() == null){ // 新增元素
            element.setId(UUID.randomUUID().toString());
            element.setCreateTime(System.currentTimeMillis());
            element.setUpdateTime(System.currentTimeMillis());
            element.setCreateUser(element.getUpdateUser());
            elementMapper.addElement(element);
        }else{ // 修改元素
            element.setUpdateTime(System.currentTimeMillis());
            elementMapper.updateElement(element);
        }

    }
    public void deleteElement(String id) {
        elementMapper.deleteElement(id);
    }

    public List<Element> getListModuleElements(String moduleId){
        return elementMapper.getListModuleElements(moduleId);
    }

    public List<ElementDTO> getElementList(QueryRequest request){
        return elementMapper.getElementList(request);
    }

}
