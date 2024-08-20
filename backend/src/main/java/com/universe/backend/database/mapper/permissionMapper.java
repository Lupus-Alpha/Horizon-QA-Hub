package com.universe.backend.database.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface permissionMapper {
    List<String> getPermissionByProjectId(String projectId);
}
