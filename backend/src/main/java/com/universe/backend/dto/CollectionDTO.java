package com.universe.backend.dto;
import com.universe.backend.database.domain.Collection;
import com.universe.backend.database.domain.CollectionCase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CollectionDTO extends Collection {

    private String username;

    private Integer caseCount;

    private List<CollectionCaseDTO> collectionCases;

}
