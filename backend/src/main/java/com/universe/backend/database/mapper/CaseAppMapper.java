package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.CaseApp;
import com.universe.backend.dto.CaseAppDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CaseAppMapper {
    List<CaseAppDTO> getCaseList(String caseId);

    void addCaseApp(List<CaseApp> caseApps);

    void deleteCaseApp(String caseId);

}
