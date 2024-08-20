package com.universe.backend.service;

import com.universe.backend.database.domain.Project;
import com.universe.backend.database.domain.User;
import com.universe.backend.request.QueryRequest;

import java.util.List;

public interface userService {
    public List<User> getUsertList(QueryRequest queryRequest);

    public void saveUser(User user);

    public void deleteUserById(String id);

    public List<User> getAllUsertList();
}
