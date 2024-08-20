package com.universe.backend.dto;


import com.universe.backend.database.domain.ReportCollection;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ReportCollectionDTO extends ReportCollection {
    private Integer passCount;  // 成功数

    private Integer failCount;  // 失败数

    private Integer errorCount; // 错误数

    private List<ReportCollectionCaseDTO> caseList; // 集合用例报告列表

}
