package com.example.testpowmanage.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.testpowmanage.common.dto.RegisterDto;
import com.example.testpowmanage.repository.UserRepository;
import com.example.testpowmanage.entity.*;
import com.example.testpowmanage.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;


import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserMapper userMapper;
    private final AllUserRoleMapper allUserRoleMapper;
    private final AllRoleMapper allRoleMapper;

    private final AllRolePermissionMapper allRolePermissionMapper;
    private final AllPermissionMapper allPermissionMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRepositoryImpl(UserMapper userMapper, AllUserRoleMapper allUserRoleMapper, AllRoleMapper allRoleMapper, AllRolePermissionMapper allRolePermissionMapper, AllPermissionMapper allPermissionMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.allUserRoleMapper = allUserRoleMapper;
        this.allRoleMapper = allRoleMapper;
        this.allRolePermissionMapper = allRolePermissionMapper;
        this.allPermissionMapper = allPermissionMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Role> selectRoleListByUsername(String username) {
        //先通过username找到这个user
        User user = userMapper.selectOne(
                new QueryWrapper<User>().eq("user_name", username)
        );
        if (user == null) {
            return List.of();
        }

        List<Long> ids = allUserRoleMapper.selectList(
                        new QueryWrapper<UserRole>().eq("user_id", user.getUserId())).stream()
                .map(UserRole::getUserId)
                .toList();
        if (ids.isEmpty()) {
            return List.of();
        }
        return allRoleMapper.selectBatchIds(ids);
    }

    @Override
    public Boolean insertUser(RegisterDto registerDto) {
        //校验all_user table中是否已经存在相同用户名
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("user_name", registerDto.getUsername());
        //不存在，则创建一个user对象插入all_user table 中
        if (userMapper.selectOne(queryWrapper) == null) {
            User user = new User(
                    null,
                    registerDto.getUsername(),
                    passwordEncoder.encode(registerDto.getPassword()),
                    registerDto.getPhone()
            );
            userMapper.insert(user);
            return true;
        }
        return false;
    }

    @Override
    public List<User> selectAllUser() {
        return userMapper.selectList(null);
    }

    @Override
    public List<Role> selectAllRole() {
        return allRoleMapper.selectList(null);
    }

    @Override
    public List<Permission> selectAllPermission() {
        return allPermissionMapper.selectList(null);
    }

    @Override
    public Boolean insertRole(String rolename) {
        //校验rolename
        if (!allRoleMapper.exists(new QueryWrapper<Role>().eq("role_name", rolename))) {
            return allRoleMapper.insert(new Role(null, rolename)) != 0;
        }
        return false;
    }

    @Override
    public List<Permission> selectPermissionByRolename(String rolename) {
        //先拿到rolename对应的roleid
        Long roleId = selectRoleIdByRolename(rolename);
        if (roleId == null) {
            return List.of();
        }
        //根据roleid找到其对应的permissionids
        List<Long> permissionIds = allRolePermissionMapper.selectList(
                        new QueryWrapper<RolePermission>().eq("role_id", roleId))
                .stream().map(RolePermission::getPermissionId)
                .toList();
        //根据ids找到list permission
        return allPermissionMapper.selectBatchIds(permissionIds);
    }

    //内置辅助方法而已
    private Long selectRoleIdByRolename(String rolename) {
        Role role = allRoleMapper.selectOne(new QueryWrapper<Role>().eq("role_name", rolename));
        if (role == null) {
            return null;
        }
        return role.getRoleId();
    }

    @Override
    public Optional<User> selectUserByUsername(String username) {
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("user_name", username);
        List<User> result = userMapper.selectByMap(wrapper);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(result.getFirst());
    }

    @Override
    public Boolean insertUserRole(String username, String rolename) {
        //找到对应的user对象
        Optional<User> userItem = selectUserByUsername(username);
        if (userItem.isEmpty()) {
            return false;
        }
        //找到对应rolename的roleid
        Long roleId = selectRoleIdByRolename(rolename);
        if (roleId == null) {
            return false;
        }
        //核验all_user_role table中是否已经存在此userrole
        if (allUserRoleMapper.exists(
                new QueryWrapper<UserRole>()
                        .allEq(Map.of
                                (
                                        "user_id", userItem.get().getUserId(),
                                        "role_id", roleId
                                )
                        )
        )) {
            return false;
        }
        //不存在则进行插入
        return allUserRoleMapper.insert(
                new UserRole(
                        null,
                        userItem.get().getUserId(),
                        roleId
                )
        ) != 0;
    }

    @Override
    public Boolean deleteUserRole(String username, String rolename) {
        //找到对应的user对象
        Optional<User> userItem = selectUserByUsername(username);
        if (userItem.isEmpty()) {
            return false;
        }
        //找到对应rolename的roleid
        Long roleId = selectRoleIdByRolename(rolename);
        if (roleId == null) {
            return false;
        }
        //删除操作
        return allUserRoleMapper.delete(
                new QueryWrapper<UserRole>().allEq(
                        Map.of(
                                "user_id", userItem.get().getUsername(),
                                "role_id", roleId
                        )
                )
        ) != 0;
    }

    @Override
    public Boolean insertRolePermission(String rolename, Long permissionId) {
        //找到对应rolename的roleid
        Long roleId = selectRoleIdByRolename(rolename);
        if (roleId == null) {
            return false;
        }
        //校验all_role_permission table中是否有对应的role permission
        if (allRolePermissionMapper.exists(
                new QueryWrapper<RolePermission>().allEq(
                        Map.of(
                                "role_id", roleId,
                                "permission_id", permissionId
                        )
                )
        )) {
            return false;
        }
        //不存在则进行插入
        return allRolePermissionMapper.insert(
                new RolePermission(
                        null,
                        roleId,
                        permissionId
                )) != 0;
    }

    @Override
    public Boolean deleteRolePermission(String rolename, Long permissionId) {
        Long roleId = selectRoleIdByRolename(rolename);
        if (roleId == null) {
            return false;
        }
        return allRolePermissionMapper.delete(
                new QueryWrapper<RolePermission>().allEq(
                        Map.of(
                                "role_id", roleId,
                                "permission_id", permissionId
                        )
                )) != 0;
    }

    @Override
    public Boolean deleteRole(String rolename) {
        Role role = allRoleMapper.selectOne(new QueryWrapper<Role>().eq("role_name", rolename));
        if (role == null) {
            return false;
        }
        allRolePermissionMapper.delete(new QueryWrapper<RolePermission>().eq("role_id", role.getRoleId()));
        allUserRoleMapper.delete(new QueryWrapper<UserRole>().eq("role_id", role.getRoleId()));
        allRoleMapper.deleteById(role.getRoleId());
        return true;
    }

    //重名情况？？？？？？？
    @Override
    public Boolean deleteUserByUsername(String username) {
        User user = userMapper.selectOne(
                new QueryWrapper<User>().select("user_id")
                        .eq("user_name", username));
        if (user == null) {
            return false;
        }
        allUserRoleMapper.delete(new QueryWrapper<UserRole>().eq("user_id", user.getUserId()));
        return userMapper.deleteById(user.getUserId()) != 0;
//        return userMapper.delete(new QueryWrapper<User>().eq("user_name",username)) !=0 ;
    }

    @Override
    public List<Role> findRolesByPermissionId(Long permissionId) {
        List<Long> roleIds = allRolePermissionMapper.findRoleIdByPermissionId(permissionId);
        if (roleIds.isEmpty()){
            return List.of();
        }
        return allRoleMapper.selectBatchIds(roleIds);
    }

    @Override
    public List<Role> listRoleByUserId(Long userId) {
        Long roleId =
                allUserRoleMapper.selectList(new QueryWrapper<UserRole>().eq("user_id", userId)).getFirst().getRoleId();
        return List.of(allRoleMapper.selectById(roleId));
    }
}
