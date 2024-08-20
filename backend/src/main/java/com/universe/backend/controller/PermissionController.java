package com.universe.backend.controller;

import com.universe.backend.common.constant.MenuEnum;
import com.universe.backend.database.mapper.permissionMapper;
import com.universe.backend.dto.MenuDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/auto_test")
public class PermissionController {
    @Resource
    private permissionMapper permissionMapper;

    @GetMapping("/menu/list")
    public List<MenuDTO> getMenu(@RequestParam String projectId){
        List<MenuDTO> result = new ArrayList<>();
        List<String> permissions = permissionMapper.getPermissionByProjectId(projectId);
        for(MenuEnum father: MenuEnum.values()){
            List<MenuDTO> son_menus = new ArrayList<>();
            if(father.parentId == null){
                for(MenuEnum son: MenuEnum.values()){
                    if(Objects.equals(son.parentId, father.id) && permissions.contains(son.permissionId)){
                        MenuDTO menuDTO = new MenuDTO();
                        menuDTO.setId(son.id);
                        menuDTO.setName(son.name);
                        menuDTO.setIcon(son.icon);
                        menuDTO.setPath(son.path);
                        son_menus.add(menuDTO);
                    }
                }
                if(son_menus.size()>0){
                    MenuDTO menuDTO = new MenuDTO();
                    menuDTO.setId(father.id);
                    menuDTO.setName(father.name);
                    menuDTO.setIcon(father.icon);
                    menuDTO.setMenus(son_menus);
                    result.add(menuDTO);

                }
            }


        }
        return result;




    }

}
