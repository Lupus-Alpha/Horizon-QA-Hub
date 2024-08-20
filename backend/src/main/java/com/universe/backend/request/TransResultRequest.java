package com.universe.backend.request;

import lombok.Data;

import java.util.List;

@Data
public class TransResultRequest {
    private String id;

    private String name;

    private String content;

    private String log;

    private Integer during; // api才有

    private Integer status;

    private List<String> screenShotList;    // web和app才有

}
