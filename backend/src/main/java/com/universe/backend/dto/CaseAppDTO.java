package com.universe.backend.dto;


import com.universe.backend.database.domain.CaseApp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseAppDTO extends CaseApp {

    private String operationName;

    private String operationType;

}
