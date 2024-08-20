package com.universe.backend.dto;

import com.universe.backend.database.domain.Case;
import com.universe.backend.database.domain.CaseApi;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CaseDTO extends Case {
    private String moduleName;

    private String username;

    private List<CaseApiDTO> caseApis;

    private List<CaseWebDTO> caseWebs;

    private List<CaseAppDTO> caseApps;


}
