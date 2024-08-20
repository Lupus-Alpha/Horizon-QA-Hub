package com.universe.backend.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EngineDTO extends Engine{
    private List<ReportDTO> tasks;
}
