package com.universe.backend.database.mapper;
import com.universe.backend.database.domain.Project;
import com.universe.backend.database.domain.User;
import com.universe.backend.dto.UserDTo;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface userMapper {
    UserDTo getUserByAccount(String account);

    void insertUser(User user);

    List<User> getUserList(QueryRequest queryRequest);

    List<User> getAllUserList();

    void deleteUserById(String id);

    UserDTo getUserById(String account);

}
