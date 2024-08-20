package com.universe.backend.service;

import com.universe.backend.database.domain.User;
import com.universe.backend.dto.RoleDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.request.UserRequest;

import java.util.List;

public interface RoleService {
    public List<RoleDTO> getRoleList(QueryRequest queryRequest);

    public List<User> getUsetList(String id);

    public void addUser(UserRequest ur);
}
