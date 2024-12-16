package com.example.testpowmanage.repository;

import com.example.testpowmanage.common.dto.RegisterDto;
import com.example.testpowmanage.entity.Permission;
import com.example.testpowmanage.entity.Role;
import com.example.testpowmanage.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<Role> selectRoleListByUsername(String username);

    Boolean insertUser(RegisterDto registerDto);

    List<User> selectAllUser();

    List<Role> selectAllRole();

    List<Permission> selectAllPermission();

    Boolean insertRole(String rolename);

    List<Permission> selectPermissionByRolename(String rolename);

    Boolean insertUserRole(String username, String rolename);


    Optional<User> selectUserByUsername(String username);

    Boolean deleteUserRole(String username, String rolename);

    Boolean insertRolePermission(String rolename, Long permissionId);

    Boolean deleteRolePermission(String rolename, Long permissionId);

    Boolean deleteRole(String rolename);

    Boolean deleteUserByUsername(String username);

    List<Role> findRolesByPermissionId(Long permissionId);

    List<Role> listRoleByUserId(Long userId);
}
