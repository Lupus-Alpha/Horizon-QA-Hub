package com.universe.backend.database.mapper;

import com.universe.backend.dto.ModuleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ModuleMapper {
    ModuleDTO getModuleByParentAndName(@Param("moduleType")String moduleType,@Param("name") String name,@Param("parentId") String parentId,@Param("projectId") String projectId);

    void saveModule(ModuleDTO moduleDTO);

    void deleteModule(String id);

    Integer getModuleDataById(@Param("moduleType")String moduleType,@Param("id") String id);

    Integer getChildModuleCount(String id);

    List<ModuleDTO> getModuleList(@Param("moduleType")String moduleType,@Param("projectId") String projectId);
}
