package com.universe.backend.database.mapper;


import com.universe.backend.database.domain.CaseWeb;
import com.universe.backend.dto.CaseWebDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CaseWebMapper {
    void addCaseWeb(List<CaseWeb> caseWebs);

    void deleteCaseWeb(String caseId);

    List<CaseWebDTO> getCaseWebList(String caseId);
}
