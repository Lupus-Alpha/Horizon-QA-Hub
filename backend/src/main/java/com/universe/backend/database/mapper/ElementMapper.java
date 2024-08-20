package com.universe.backend.database.mapper;


import com.universe.backend.database.domain.Element;
import com.universe.backend.dto.ElementDTO;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ElementMapper {
    void addElement(Element element);

    void updateElement(Element element);

    void deleteElement(String id);

    List<Element> getListModuleElements(String moduleId);

    List<ElementDTO> getElementList(QueryRequest request);

    ElementDTO getElementById(String id);

}
