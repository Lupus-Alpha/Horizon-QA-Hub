package com.universe.backend.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.Project;
import com.universe.backend.database.domain.User;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.UserServiceIml;
import com.universe.backend.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auto_test/user")
public class UserController {
    @Autowired
    UserServiceIml userServiceIml;

    @PostMapping("/add")
    public void addUser(@RequestBody User user){
        userServiceIml.saveUser(user);
    }

    @PostMapping("/list/{page_num}/{page_size}")
    public PageDTO<List<User>> getUserList(@PathVariable int page_num, @PathVariable int page_size, @RequestBody QueryRequest queryRequest){
        Page<Object> page= PageHelper.startPage(page_num,page_size,true);
        return PageUtils.setPageInfo(page, userServiceIml.getUsertList(queryRequest));

    }

    @PostMapping("/delete/{id}")
    public void deleteUser(@PathVariable String id){
        userServiceIml.deleteUserById(id);

    }

    @GetMapping("/all/list")
    public List<User> getAllUser(){
        return userServiceIml.getAllUsertList();
    }


}
