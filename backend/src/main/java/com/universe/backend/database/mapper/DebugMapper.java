package com.universe.backend.database.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DebugMapper {
    String getDebugData(String id);
}
