package com.universe.backend.dto;


import com.universe.backend.database.domain.ReportCollectionCase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ReportCollectionCaseDTO extends ReportCollectionCase {

    private List<ReportCollectionCaseTransDTO> transList;   // 用例步骤事务的列表 但是批量获取报告时需要懒加载

}
