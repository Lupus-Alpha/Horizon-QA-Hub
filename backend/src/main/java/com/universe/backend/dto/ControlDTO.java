package com.universe.backend.dto;

import com.universe.backend.database.domain.Control;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControlDTO extends Control {
    private String moduleName;

}
