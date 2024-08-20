package com.universe.backend.dto;

import com.universe.backend.database.domain.PlanCollection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanCollectionDTO extends PlanCollection {
    private String collectionName;

    private String collectionDescription;
}
