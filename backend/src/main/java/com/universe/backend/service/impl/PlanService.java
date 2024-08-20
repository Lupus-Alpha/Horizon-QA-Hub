package com.universe.backend.service.impl;

import com.universe.backend.common.exception.DemoException;
import com.universe.backend.database.domain.PlanCollection;
import com.universe.backend.database.mapper.CollectionCaseMapper;
import com.universe.backend.database.mapper.planCollectionMapper;
import com.universe.backend.database.mapper.planMapper;
import com.universe.backend.dto.PlanCollectionDTO;
import com.universe.backend.dto.PlanDTO;
import com.universe.backend.request.QueryRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PlanService {
    @Resource
    private CollectionCaseMapper collectionCaseMapper;

    @Resource
    private planCollectionMapper planCollectionMapper;

    @Resource
    private planMapper planMapper;

    public void savePlan(PlanDTO planDTO) {
        //API或WEB用例,环境不能为空
        if(planDTO.getEnvironmentId()==null || planDTO.getEnvironmentId().equals("")){
            for(PlanCollectionDTO planCollectionDTO: planDTO.getPlanCollections()){
                List<String> caseTypes = collectionCaseMapper.getCollectionCaseTypes(planCollectionDTO.getCollectionId());
                if(caseTypes.contains("API") || caseTypes.contains("WEB")){
                    throw new DemoException("所选集合中存在API或WEB用例 环境不能为空");
                }
            }
        }
        if(planDTO.getId().equals("") || planDTO.getId() == null){ // 新增计划
            planDTO.setId(UUID.randomUUID().toString());
            planDTO.setCreateTime(System.currentTimeMillis());
            planDTO.setUpdateTime(System.currentTimeMillis());
            planDTO.setCreateUser(planDTO.getUpdateUser());
            planMapper.addPlan(planDTO);
        }else{
            planDTO.setUpdateTime(System.currentTimeMillis());
            planMapper.updatePlan(planDTO);
        }

        List<PlanCollection> planCollections = new ArrayList<>();
        for(PlanCollectionDTO planCollectionDTO: planDTO.getPlanCollections()){
            PlanCollection planCollection = new PlanCollection();
            planCollection.setId(UUID.randomUUID().toString());
            planCollection.setPlanId(planDTO.getId());
            planCollection.setCollectionId(planCollectionDTO.getCollectionId());
            planCollections.add(planCollection);
        }
        planCollectionMapper.deletePlanCollection(planDTO.getId());  //先删除全部计划集合
        if(planCollections.size() > 0) {
            planCollectionMapper.addPlanCollection(planCollections);
        }

    }

    public void deletePlan(String id) {
        planMapper.deletePlan(id);
    }

    public List<PlanDTO> getPlanList(QueryRequest request){
        if(request.getCondition() != null && !request.getCondition().equals("")){
            request.setCondition("%"+request.getCondition()+"%");
        }
        return planMapper.getPlanList(request);
    }
}
