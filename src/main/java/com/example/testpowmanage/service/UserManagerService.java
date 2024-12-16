package com.example.testpowmanage.service;


import com.example.testpowmanage.common.dto.RegisterDto;
import com.example.testpowmanage.common.vo.UserVO;
import com.example.testpowmanage.entity.Permission;
import com.example.testpowmanage.entity.Role;
import com.example.testpowmanage.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserManagerService {

   List<Role> findRoleByUsername(String username);

   Boolean register(RegisterDto registerDto);

   List<UserVO> listAllUser();

   List<Role> listAllRole();

   List<Permission> listAllPermission();

   List<Role> selectRoleByUsername(String username);

   Boolean createRole(String rolename);

   List<Permission> selectPermissionByRolename(String rolename);

   Boolean changeRole(String username, Map<String, Boolean> changeMap);

   Boolean changePermission(String rolename, Map<Long, Boolean> changeMap);

   Boolean deleteRole(String rolename);

   Boolean closeUser(String username);
}
