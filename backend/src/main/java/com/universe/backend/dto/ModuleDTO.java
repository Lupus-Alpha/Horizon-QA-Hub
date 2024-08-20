package com.universe.backend.dto;

import com.universe.backend.database.domain.Moudle;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ModuleDTO extends Moudle {
    private List<ModuleDTO> children;

    private String label;   // 等于name

    private String moduleType;
}
