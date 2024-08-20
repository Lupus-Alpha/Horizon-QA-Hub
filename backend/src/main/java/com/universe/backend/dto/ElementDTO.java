package com.universe.backend.dto;


import com.universe.backend.database.domain.Element;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElementDTO extends Element {
    private String moduleName;
}
