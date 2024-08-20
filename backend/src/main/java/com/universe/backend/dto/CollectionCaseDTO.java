package com.universe.backend.dto;


import com.universe.backend.database.domain.CollectionCase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionCaseDTO extends CollectionCase {
    private String caseName;

    private String caseModule;

    private String caseType;

    private String caseSystem;
}
