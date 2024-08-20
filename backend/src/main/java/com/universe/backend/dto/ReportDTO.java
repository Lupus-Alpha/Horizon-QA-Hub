package com.universe.backend.dto;

import com.universe.backend.database.domain.Report;
import com.universe.backend.database.domain.ReportCollection;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ReportDTO extends Report {

    private String username;    // 执行人名字

    private Integer total;

    private Integer passCount;

    private Integer failCount;

    private Integer errorCount;

    private String passRate;

    private Integer progress;// 当前执行进度

    private List<ReportCollectionDTO> collectionList;   // 报告集合列表

}
