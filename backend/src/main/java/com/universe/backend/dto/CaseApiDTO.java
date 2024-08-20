package com.universe.backend.dto;


import com.universe.backend.database.domain.CaseApi;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseApiDTO extends CaseApi {
    private String apiName;

    private String apiPath;

    private String apiMethod;

    private String apiProtocol;

    private String apiServerSign;
}
