package com.universe.backend.database.domain;

import lombok.Data;

@Data
public class CollectionCase {
    private String id;
    private String collectionId;
    private String caseId;
    private Long index;
}
