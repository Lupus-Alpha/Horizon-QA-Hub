package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.Device;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {
    void saveDevice(Device device);

    void updateDevice(Device device);

    Device getDeviceBySerial(String serial);

    Device getDeviceById(String id);

    List<Device> getDeviceList(QueryRequest request);

    List<Device> getDeviceListByProjectId(@Param("projectId") String projectId, @Param("system") String system);
}
