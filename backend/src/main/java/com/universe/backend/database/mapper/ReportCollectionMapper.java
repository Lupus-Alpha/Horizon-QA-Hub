package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.ReportCollection;
import com.universe.backend.dto.ReportCollectionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportCollectionMapper {
    void addReportCollection(List<ReportCollection> reportCollections);

    ReportCollection getReportCollection(@Param("reportId") String reportId,@Param("collectionId") String collectionId);

    List<ReportCollectionDTO> getReportCollectionList(@Param("reportId") String reportId);


}
