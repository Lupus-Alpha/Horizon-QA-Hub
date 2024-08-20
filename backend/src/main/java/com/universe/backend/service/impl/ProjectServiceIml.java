package com.universe.backend.service.impl;

import com.universe.backend.common.constant.PermissionEnum;
import com.universe.backend.database.domain.Project;
import com.universe.backend.database.domain.Role;
import com.universe.backend.database.domain.RolePermission;
import com.universe.backend.database.mapper.RoleMapper;
import com.universe.backend.database.mapper.projectMapper;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.ProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectServiceIml implements ProjectService {
    @Resource
    projectMapper projectMapper;

    @Resource
    RoleMapper roleMapper;

    @Override
    public List<Project> getProjectList(QueryRequest queryRequest) {
        if(queryRequest.getCondition() != null && !queryRequest.getCondition().equals("")){
            queryRequest.setCondition("%"+queryRequest.getCondition()+"%");
        }

        return projectMapper.getProjectList(queryRequest);
    }

    @Override
    public void saveProject(Project project) {
        if(project.getId() ==null || project.getId().equals("")){
            //添加项目
            project.setId(UUID.randomUUID().toString());
            project.setCreateTime(System.currentTimeMillis());
            project.setUpdateTime(System.currentTimeMillis());
            projectMapper.insertProject(project);

            Role adminRole = new Role();
            adminRole.setId(UUID.randomUUID().toString());
            adminRole.setName("项目管理员");
            adminRole.setProjectId(project.getId());
            adminRole.setCreateTime(System.currentTimeMillis());
            adminRole.setUpdateTime(System.currentTimeMillis());
            roleMapper.insertRole(adminRole);
            Role normalRole = new Role();
            normalRole.setId(UUID.randomUUID().toString());
            normalRole.setName("项目普通用户");
            normalRole.setProjectId(project.getId());
            normalRole.setCreateTime(System.currentTimeMillis());
            normalRole.setUpdateTime(System.currentTimeMillis());
            roleMapper.insertRole(normalRole);
            // 给角色添加权限
            List<RolePermission> rolePermissions = new ArrayList<>();
            for(PermissionEnum permissionEnum: PermissionEnum.values()){
                if(permissionEnum.equals(PermissionEnum.PROJECT_MENU)){
                    // 项目管理菜单只有系统管理员才有
                    continue;
                }
                if(permissionEnum.equals(PermissionEnum.NORMAL_MENU) || permissionEnum.equals(PermissionEnum.SETTING_MENU)){
                    // 平台常规菜单和设置查看权限 管理员和普通用户都有
                    rolePermissions.add(this.getRolePermission(adminRole.getId(), permissionEnum.id));
                    rolePermissions.add(this.getRolePermission(normalRole.getId(), permissionEnum.id));
                }else {
                    // 其他菜单和权限则只有管理员有权限
                    rolePermissions.add(this.getRolePermission(adminRole.getId(), permissionEnum.id));
                }
            }
            roleMapper.insertRolePermissions(rolePermissions);
        }else {
            // 编辑项目
            project.setUpdateTime(System.currentTimeMillis());
//            projectMapper.updateProject(project);
        }

    }

    private RolePermission getRolePermission(String roleId, String permissionId){
        RolePermission rolePermission = new RolePermission();
        rolePermission.setId(UUID.randomUUID().toString());
        rolePermission.setRoleId(roleId);
        rolePermission.setPermissionId(permissionId);
        rolePermission.setCreateTime(System.currentTimeMillis());
        rolePermission.setUpdateTime(System.currentTimeMillis());
        return rolePermission;
    }
}
