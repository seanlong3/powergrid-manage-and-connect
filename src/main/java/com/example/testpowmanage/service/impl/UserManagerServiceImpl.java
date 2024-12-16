package com.example.testpowmanage.service.impl;

import com.example.testpowmanage.common.dto.RegisterDto;
import com.example.testpowmanage.common.vo.UserVO;
import com.example.testpowmanage.repository.UserRepository;
import com.example.testpowmanage.entity.Permission;
import com.example.testpowmanage.entity.Role;
import com.example.testpowmanage.entity.User;
import com.example.testpowmanage.service.UserManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserManagerServiceImpl implements UserManagerService {

    private final UserRepository userRepository;

    @Autowired
    public UserManagerServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<Role> findRoleByUsername(String username) {
        return userRepository.selectRoleListByUsername(username);
    }

    @Override
    public Boolean register(RegisterDto registerDto) {
        return userRepository.insertUser(registerDto);
    }

    @Override
    public List<UserVO> listAllUser() {
        //主要需要将数据库中的user转换成前端需要的uservo形式
        //先从all_user table中拿到所有的user放到list中
        List<User> userList = userRepository.selectAllUser();
        List<UserVO> userVOList = new ArrayList<>();
        for (User userItem : userList) {
            //先拿到单个user的rolelist再转化为需要的list<string>形式
            List<String> roleList = userRepository.selectRoleListByUsername(userItem.getUsername())
                    .stream().map(Role::getRoleName).toList();
            //将单个user的数据转化成uservo形式加入结果集中
            userVOList.add(new UserVO(
                    userItem.getUserId(),
                    userItem.getUsername(),
                    userItem.getPhone(),
                    roleList
            ));
        }
        return userVOList;
    }

    @Override
    public List<Role> listAllRole() {
        return userRepository.selectAllRole();
    }

    @Override
    public List<Permission> listAllPermission() {
        return userRepository.selectAllPermission();
    }

    @Override
    public List<Role> selectRoleByUsername(String username) {
        return userRepository.selectRoleListByUsername(username);
    }

    @Override
    public Boolean createRole(String rolename) {
        return userRepository.insertRole(rolename);
    }

    @Override
    public List<Permission> selectPermissionByRolename(String rolename) {
        return userRepository.selectPermissionByRolename(rolename);
    }


    @Override
    public Boolean changeRole(String username, Map<String, Boolean> changeMap) {
        //遍历changeMap
        for (Map.Entry<String, Boolean> entry : changeMap.entrySet()) {
            //如果对应的value为true则进行change,即添加对应的角色给用户
            if (entry.getValue()) {
                if (!userRepository.insertUserRole(username, entry.getKey())) {
                    return false;
                }
            } else {
                if (!userRepository.deleteUserRole(username, entry.getKey())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Boolean changePermission(String rolename, Map<Long, Boolean> changeMap) {
        for (Map.Entry<Long, Boolean> entry : changeMap.entrySet()) {
            if (entry.getValue()) {
                if (!userRepository.insertRolePermission(rolename, entry.getKey())) {
                    return false;
                }
            } else {
                if (!userRepository.deleteRolePermission(rolename, entry.getKey())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Boolean deleteRole(String rolename) {
        return userRepository.deleteRole(rolename);
    }

    @Override
    public Boolean closeUser(String username) {
        return userRepository.deleteUserByUsername(username);
    }
}
