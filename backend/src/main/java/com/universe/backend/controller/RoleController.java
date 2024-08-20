package com.universe.backend.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.Role;
import com.universe.backend.database.domain.User;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.dto.RoleDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.request.UserRequest;
import com.universe.backend.service.impl.RoleServiceIml;
import com.universe.backend.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auto_test/role")
public class RoleController {
    @Autowired
    RoleServiceIml roleService;


    @PostMapping("/list/{page_num}/{page_size}")
    public PageDTO<List<RoleDTO>> getroleList(@PathVariable int page_num, @PathVariable int page_size, @RequestBody QueryRequest queryRequest){
        Page<Object> page= PageHelper.startPage(page_num,page_size,true);
        return PageUtils.setPageInfo(page, roleService.getRoleList(queryRequest));

    }

    @GetMapping("/user/list/{id}")
    public List<User> getUser(@PathVariable String id){
        return roleService.getUsetList(id);

    }

    @PostMapping("/add/user")
    public void addUser(@RequestBody UserRequest ur){
        roleService.addUser(ur);

    }


}
