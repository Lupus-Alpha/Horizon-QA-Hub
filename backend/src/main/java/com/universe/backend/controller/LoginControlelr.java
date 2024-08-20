package com.universe.backend.controller;

import com.universe.backend.database.domain.User;
import com.universe.backend.database.mapper.permissionMapper;
import com.universe.backend.database.mapper.projectMapper;
import com.universe.backend.database.mapper.userMapper;
import com.universe.backend.dto.UserDTo;
import com.universe.backend.utils.JwtUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.universe.backend.common.exception.LoginException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/auto_test")
public class LoginControlelr {

    @Resource
    private userMapper userMapper;

    @Resource
    private projectMapper projectMapper;

    @Resource
    private permissionMapper  permissionMapper;

    @PostMapping("/login")
    public UserDTo login(@RequestBody User user, HttpServletResponse response){
       String account= user.getAccount();
       UserDTo u =userMapper.getUserByAccount(account);
       if(u == null){
            throw new LoginException("用户名不存在");
       }
        if(!u.getPassword().equals(user.getPassword())){
            throw new LoginException("密码不正确");
        }
        response.setHeader("token", JwtUtils.createPlatformToken(user));
        String projectId = projectMapper.getProjectIdByUserId(u.getId());
        System.out.println(projectId);
        List<String> permissions = permissionMapper.getPermissionByProjectId(projectId);
        System.out.println(permissions);
        u.setPermission(permissions);

        return u;



    }
}
