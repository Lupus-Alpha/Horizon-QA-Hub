package com.universe.backend.dto;

import com.universe.backend.database.domain.Role;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoleDTO extends Role {
    private String projectName;

}
