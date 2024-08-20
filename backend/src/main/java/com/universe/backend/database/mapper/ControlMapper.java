package com.universe.backend.database.mapper;


import com.universe.backend.database.domain.Control;
import com.universe.backend.dto.ControlDTO;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ControlMapper {
    void addControl(Control control);

    void updateControl(Control control);

    void deleteControl(String id);

    List<Control> getListModuleControls(String system, String moduleId);

    List<ControlDTO> getControlList(QueryRequest request);

    ControlDTO getControlById(String id);
}
