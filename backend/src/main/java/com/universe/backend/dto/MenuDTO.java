package com.universe.backend.dto;


import lombok.Data;

import java.util.List;

@Data
public class MenuDTO {
    private Integer id;
    private String name;
    private String icon;
    private Integer parentId;
    private String path;
    private List<MenuDTO> menus;


}
