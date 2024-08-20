package com.universe.backend.service.impl;


import com.universe.backend.database.domain.Case;
import com.universe.backend.database.domain.CaseApi;
import com.universe.backend.database.domain.CaseApp;
import com.universe.backend.database.domain.CaseWeb;
import com.universe.backend.database.mapper.CaseApiMapper;
import com.universe.backend.database.mapper.CaseAppMapper;
import com.universe.backend.database.mapper.CaseMapper;
import com.universe.backend.database.mapper.CaseWebMapper;
import com.universe.backend.dto.CaseApiDTO;
import com.universe.backend.dto.CaseAppDTO;
import com.universe.backend.dto.CaseDTO;
import com.universe.backend.dto.CaseWebDTO;
import com.universe.backend.request.QueryRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CaseService {
    @Resource
    private CaseMapper caseMapper;

    @Resource
    private CaseApiMapper caseApiMapper;

    @Resource
    private CaseWebMapper caseWebMapper;

    @Resource
    private CaseAppMapper caseAppMapper;

    public void saveCase(CaseDTO caseDTO) {
        if(caseDTO.getId().equals("") || caseDTO.getId() == null){
            caseDTO.setId(UUID.randomUUID().toString());
            caseDTO.setCreateTime(System.currentTimeMillis());
            caseDTO.setUpdateTime(System.currentTimeMillis());
            caseDTO.setCreateUser(caseDTO.getUpdateUser());
            caseMapper.addCase(caseDTO);
        }else{
            caseDTO.setUpdateTime(System.currentTimeMillis());
            caseMapper.updateCase(caseDTO);
        }
        if(caseDTO.getType().equals("API")){
            caseApiMapper.deleteCaseApi(caseDTO.getId());
            List<CaseApiDTO> caseApiArray = caseDTO.getCaseApis();
            List<CaseApi> caseApis = new ArrayList<>();
            for(CaseApi caseApi: caseApiArray){
                caseApi.setCaseId(caseDTO.getId());
                caseApi.setId(UUID.randomUUID().toString());
                caseApis.add(caseApi);
            }
            caseApiMapper.addCaseApi(caseApis);
        }else if(caseDTO.getType().equals("WEB")){
            caseWebMapper.deleteCaseWeb(caseDTO.getId());  //先删除全部用例操作
            List<CaseWebDTO> caseWebArray = caseDTO.getCaseWebs();
            List<CaseWeb> caseWebs = new ArrayList<>();
            for(CaseWebDTO caseWebDTO: caseWebArray){
                caseWebDTO.setCaseId(caseDTO.getId());
                caseWebDTO.setId(UUID.randomUUID().toString());
                caseWebs.add(caseWebDTO);
            }
            caseWebMapper.addCaseWeb(caseWebs);
        }else{
            caseAppMapper.deleteCaseApp(caseDTO.getId());  //先删除全部用例接口
            List<CaseAppDTO> caseAppArray = caseDTO.getCaseApps();
            List<CaseApp> caseApps = new ArrayList<>();
            for(CaseAppDTO caseAppDTO: caseAppArray){
                caseAppDTO.setCaseId(caseDTO.getId());
                caseAppDTO.setId(UUID.randomUUID().toString());
                caseApps.add(caseAppDTO);
            }
            caseAppMapper.addCaseApp(caseApps);

        }

    }

    public List<CaseDTO> getCaseList(QueryRequest request) {
        if(request.getCondition() != null && !request.getCondition().equals("")){
            request.setCondition("%"+request.getCondition()+"%");
        }
        return caseMapper.getCaseList(request);
    }

    public CaseDTO getCaseDetail(String caseType,String id) {

        CaseDTO case_dto = caseMapper.getCaseDetail(caseType,id);
        if(caseType.equalsIgnoreCase("api")){
            List<CaseApiDTO> caseApis = caseApiMapper.getCaseApiList(id);
            case_dto.setCaseApis(caseApis);

        }

        return case_dto;
    }

}
