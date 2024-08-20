package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.ReportCollectionCaseApi;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportCollectionCaseApiMapper {

    void deleteByReportCaseId(String reportCaseId);

    void batchAddReportCollectionCaseApi(List<ReportCollectionCaseApi> reportCollectionCaseApis);
}
