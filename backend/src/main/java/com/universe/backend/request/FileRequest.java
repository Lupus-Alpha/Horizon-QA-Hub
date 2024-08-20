package com.universe.backend.request;

import lombok.Data;

@Data
public class FileRequest {
    private String id;
    private String name;
    private String description;
    private String projectId;
}
