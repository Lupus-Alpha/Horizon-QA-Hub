package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.Report;
import com.universe.backend.database.domain.ReportStatistics;
import com.universe.backend.dto.ReportDTO;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper {
    void addReport(Report report);
    List<ReportDTO> getReportList(QueryRequest request);
    Report getToRunReport(String engineId);
    void updateReportToRunning(Report report);
    Report getReportById(String id);

    ReportDTO getReportDetail(String reportId);

    void updateReportEndTime(@Param("reportId")String reportId, @Param("endTime")Long endTime,@Param("updateTime") Long updateTime);

    void updateReportStatistics(ReportStatistics reportStatistics);

    ReportStatistics getReportStatistics(String reportId);

    void updateReportStatus(@Param("status")String status,@Param("id") String id);

}
