package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.PlanCollection;
import com.universe.backend.dto.CollectionDTO;
import com.universe.backend.dto.PlanCollectionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface planCollectionMapper {
    void addPlanCollection(List<PlanCollection> planCollections);

    void deletePlanCollection(String planId);

    List<CollectionDTO> getPlanCollections(String planId);

}
