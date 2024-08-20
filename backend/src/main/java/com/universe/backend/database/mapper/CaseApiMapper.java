package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.CaseApi;
import com.universe.backend.dto.CaseApiDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CaseApiMapper {
    void addCaseApi(List<CaseApi> caseApis);

    void deleteCaseApi(String  caseId);

    List<CaseApiDTO> getCaseApiList(String caseId);
}
