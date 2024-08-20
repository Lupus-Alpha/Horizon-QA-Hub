package com.universe.backend.dto;

import lombok.Data;


@Data
public class ReportCollectionCaseTransDTO {

    private String status;

    private String transId;

    private String transName;

    private String content; // api的path或UI的element

    private String execLog; // 执行日志

    private String during;  // api

    private String screenshotList;  // Ui

    private Boolean showViewer = false;

}
