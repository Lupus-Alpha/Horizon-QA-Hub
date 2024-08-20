package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.Role;
import com.universe.backend.database.domain.RolePermission;
import com.universe.backend.database.domain.User;
import com.universe.backend.database.domain.UserRole;
import com.universe.backend.dto.RoleDTO;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface RoleMapper {
    void insertRole(Role role);

    void insertRolePermissions(List<RolePermission> rolePermissions);

    void deleteRoleUser(UserRole userRole);

    void insertRoleUser(List<UserRole> userRoles);

    List<RoleDTO> getRoleList(QueryRequest queryRequest);

    List<User> getRoleUser(String roleId);

    Integer countRoleUser(String roleId, String userId);

    Role getRoleById(String id);

    Integer countRoleUserByProject(String projectId, String userId);
}
