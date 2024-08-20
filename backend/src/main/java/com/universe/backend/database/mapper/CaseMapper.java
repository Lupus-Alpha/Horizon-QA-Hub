package com.universe.backend.database.mapper;

import com.universe.backend.dto.CaseDTO;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CaseMapper {
    void addCase(CaseDTO caseDTO);

    void updateCase(CaseDTO caseDTO);

    List<CaseDTO> getCaseList(QueryRequest request);

    CaseDTO getCaseDetail(@Param("type") String type,@Param("id") String id);

    CaseDTO getCaseById(String id);
}
