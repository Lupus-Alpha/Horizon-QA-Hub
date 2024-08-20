package com.universe.backend.service.impl;

import com.universe.backend.database.domain.Role;
import com.universe.backend.database.domain.User;
import com.universe.backend.database.domain.UserRole;
import com.universe.backend.database.mapper.RoleMapper;
import com.universe.backend.dto.RoleDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.request.UserRequest;
import com.universe.backend.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceIml implements RoleService {
    @Resource
    RoleMapper roleMapper;


    @Override
    public List<RoleDTO> getRoleList(QueryRequest queryRequest) {
        if(queryRequest.getCondition() != null && !queryRequest.getCondition().equals("")){
            queryRequest.setCondition("%"+queryRequest.getCondition()+"%");
        }
       return roleMapper.getRoleList(queryRequest);


    }

    @Override
    public List<User> getUsetList(String id) {
        return roleMapper.getRoleUser(id);

    }

    @Override
    public void addUser(UserRequest ur) {
        List<UserRole>  userRoleList = new ArrayList<>();
        for(String id : ur.getUserIds()){
            UserRole userRole = new UserRole();
            userRole.setId(UUID.randomUUID().toString());
            userRole.setUserId(id);
            userRole.setRoleId(ur.getRoleId());
            userRole.setCreateTime(System.currentTimeMillis());
            userRole.setUpdateTime(System.currentTimeMillis());
            userRoleList.add(userRole);
        }
        roleMapper.insertRoleUser(userRoleList);
    }
}
