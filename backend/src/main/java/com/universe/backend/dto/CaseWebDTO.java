package com.universe.backend.dto;


import com.universe.backend.database.domain.CaseWeb;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseWebDTO extends CaseWeb {

    private String operationName;

    private String operationType;

}
