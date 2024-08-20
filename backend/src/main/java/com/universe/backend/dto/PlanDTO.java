package com.universe.backend.dto;


import com.universe.backend.database.domain.Plan;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class PlanDTO extends Plan {

    private List<PlanCollectionDTO> planCollections;

}
