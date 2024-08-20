package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.ReportCollectionCase;
import com.universe.backend.dto.ReportCollectionCaseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportCollectionCaseMapper {
    void addReportCollectionCase(ReportCollectionCase reportCollectionCase);

    void updateReportCollectionCase(ReportCollectionCase reportCollectionCase);

    Integer countReportResult(@Param("status")String status,@Param("reportId") String reportId);

    Integer countReportCollectionResult(@Param("status")String status, @Param("reportCollectionId") String reportCollectionId);

    String getReportCollectionCaseId(@Param("reportCollectionId")String reportCollectionId, @Param("index")Integer index);

    List<ReportCollectionCaseDTO> getReportCollectionCaseList(String reportCollectionId);



}
