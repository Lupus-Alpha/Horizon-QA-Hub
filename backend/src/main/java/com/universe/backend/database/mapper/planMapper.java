package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.Plan;
import com.universe.backend.dto.PlanDTO;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface planMapper {
    void addPlan(Plan plan);

    void updatePlan(Plan plan);

    void deletePlan(String id);

    List<PlanDTO> getPlanList(QueryRequest request);

}
