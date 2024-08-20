package com.universe.backend.database.mapper;


import com.universe.backend.database.domain.ReportCollectionCaseWeb;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportCollectionCaseWebMapper {
    void batchAddReportCollectionCaseWeb(List<ReportCollectionCaseWeb> reportCollectionCaseWebs);

//    List<ReportCollectionCaseTransDTO> getReportCaseTransList(String reportCaseId);

    void deleteByReportId(String reportId);

    void deleteByReportCaseId(String reportCaseId);
}
