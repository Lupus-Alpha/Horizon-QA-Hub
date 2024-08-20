package com.universe.backend.service.impl;

import com.universe.backend.common.exception.DemoException;
import com.universe.backend.database.domain.CollectionCase;
import com.universe.backend.database.domain.Device;
import com.universe.backend.database.mapper.CollectionCaseMapper;
import com.universe.backend.database.mapper.CollectionMapper;
import com.universe.backend.database.mapper.DeviceMapper;
import com.universe.backend.dto.CollectionCaseDTO;
import com.universe.backend.dto.CollectionDTO;
import com.universe.backend.request.QueryRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CollectionService {
    @Resource
    private CollectionMapper collectionMapper;

    @Resource
    private CollectionCaseMapper collectionCaseMapper;

    @Resource
    private DeviceMapper deviceMapper;

    public void saveCollection(CollectionDTO collectionDTO) {
        boolean hasAndroidCase = false;
        boolean hasAppleCase = false;
        for(CollectionCaseDTO collectionCaseDTO :collectionDTO.getCollectionCases()){
            if("android".equals(collectionCaseDTO.getCaseSystem())){
                hasAndroidCase = true;
            }
            if("apple".equals(collectionCaseDTO.getCaseSystem())){
                hasAppleCase = true;
            }
        }
        if(hasAndroidCase && hasAppleCase){
            throw new RuntimeException("同一测试集合不能同时选择苹果和安卓用例");
        }
        Device device = deviceMapper.getDeviceById(collectionDTO.getDeviceId());
        if(hasAndroidCase && !device.getSystem().equals("android")){
            throw new DemoException("安卓用例不支持在非安卓设备上执行");
        }
        if(hasAppleCase && !device.getSystem().equals("apple")){
            throw new DemoException("苹果用例不支持在非苹果设备上执行");
        }

        if(collectionDTO.getId().equals("") || collectionDTO.getId() == null) {
            collectionDTO.setCreateTime(System.currentTimeMillis());
            collectionDTO.setUpdateTime(System.currentTimeMillis());
            collectionDTO.setCreateUser(collectionDTO.getUpdateUser());
            collectionDTO.setId(UUID.randomUUID().toString());
            collectionDTO.setTotal(collectionDTO.getCollectionCases().size());
            collectionMapper.saveCollection(collectionDTO);

        } else {
            collectionDTO.setTotal(collectionDTO.getCollectionCases().size());
            collectionDTO.setUpdateTime(System.currentTimeMillis());
            collectionMapper.updateCollection(collectionDTO);
        }
        collectionCaseMapper.deleteCollectionCase(collectionDTO.getId());
        ArrayList<CollectionCase> collectionCases = new ArrayList<>();
        for(CollectionCaseDTO collectionCaseDTO :collectionDTO.getCollectionCases()){
            CollectionCase collectionCase = new CollectionCase();
            collectionCase.setId(UUID.randomUUID().toString());
            collectionCase.setCollectionId(collectionDTO.getId());
            collectionCase.setCaseId(collectionCaseDTO.getCaseId());
            collectionCase.setIndex(collectionCaseDTO.getIndex());
            collectionCases.add(collectionCase);

        }
        if(collectionCases.size() > 0) {
            collectionCaseMapper.saveCollectionCase(collectionCases);
        }

    }

    public List<CollectionDTO> getCollectionList(QueryRequest request) {
        if(request.getCondition() != null && !request.getCondition().equals("")){
            request.setCondition("%"+request.getCondition()+"%");
        }
        return collectionMapper.getCollectionList(request);
    }

    public Map<String, Boolean> getCollectionCaseTypes(String collectionId){
        List<String> collectionCaseTypes = collectionCaseMapper.getCollectionCaseTypes(collectionId);
        Map<String, Boolean> result = new HashMap<>();
        for(String collectionCaseType : collectionCaseTypes){
            if("android".equals(collectionCaseType)){
                result.put("android", true);
            }else if("apple".equals(collectionCaseType)){
                result.put("apple", true);
            }else {
                result.put("needEnvironment", true);
            }

        }
        return result;

    }

    public CollectionDTO getCollectionDetail(String collectionId){
        CollectionDTO collectionDTO = collectionMapper.getCollectionDetail(collectionId);
        List<CollectionCaseDTO> collectionCaseDTOS = collectionCaseMapper.getCollectionCaseList(collectionId);
        collectionDTO.setCollectionCases(collectionCaseDTOS);
        return collectionDTO;
    }
}
