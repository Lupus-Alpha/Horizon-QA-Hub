package com.universe.backend.service.impl;

import com.universe.backend.common.exception.DemoException;
import com.universe.backend.database.domain.Project;
import com.universe.backend.database.domain.User;
import com.universe.backend.database.mapper.userMapper;
import com.universe.backend.dto.UserDTo;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.userService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceIml implements userService {
    @Resource
    userMapper userMapper;

    @Override
    public List<User> getUsertList(QueryRequest queryRequest) {
        if(queryRequest.getCondition() != null && !queryRequest.getCondition().equals("")){
            queryRequest.setCondition("%"+queryRequest.getCondition()+"%");
        }

        return userMapper.getUserList(queryRequest);
    }

    @Override
    public void saveUser(User user) {
        UserDTo ud = userMapper.getUserByAccount(user.getAccount());
        if(ud !=null){
            throw new DemoException("登录账号已存在");
        }
        user.setId(UUID.randomUUID().toString());
        user.setPassword("123456");
        user.setCreateTime(System.currentTimeMillis());
        user.setUpdateTime(System.currentTimeMillis());
        userMapper.insertUser(user);


    }

    @Override
    public void deleteUserById(String id) {
        UserDTo userDTo= userMapper.getUserById(id);
        if(userDTo == null){
            throw new DemoException("用户不存在");
        }
        userMapper.deleteUserById(id);
    }

    @Override
    public List<User> getAllUsertList() {
        return userMapper.getAllUserList();
    }
}
