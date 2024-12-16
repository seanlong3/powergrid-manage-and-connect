package com.example.testpowmanage.controller;

import com.example.testpowmanage.common.dto.ChangePermissionDto;
import com.example.testpowmanage.common.dto.ChangeRoleDto;
import com.example.testpowmanage.common.dto.RegisterDto;
import com.example.testpowmanage.common.vo.UserVO;
import com.example.testpowmanage.entity.Permission;
import com.example.testpowmanage.entity.Role;
import com.example.testpowmanage.service.UserManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserManagerController {
    private final UserManagerService userManagerService;

    @Autowired
    public UserManagerController(UserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

    @PostMapping("/register")
    public Boolean register(@RequestBody RegisterDto registerDto){
        return userManagerService.register(registerDto);
    }

    @GetMapping("/listalluser")
    public List<UserVO> listAllUser(){
        return userManagerService.listAllUser();
    }

    @GetMapping("/listallrole")
    public List<Role> listAllRole(){
        return userManagerService.listAllRole();
    }

    @GetMapping("/listallpermission")
    public List<Permission> listAllPermission(){
        return userManagerService.listAllPermission();
    }

    @PostMapping("/getrolebyusername")
    public List<Role> getRoleByUsername(@RequestParam("username") String username){
        return userManagerService.selectRoleByUsername(username);
    }

    @RequestMapping("/createrole")
    public Boolean createRole(@RequestParam("rolename") String rolename){
        return userManagerService.createRole(rolename);
    }

    @PostMapping("/getpermissionbyrolename")
    public List<Permission> getPermissionByRolename(@RequestParam("rolename") String rolename){
        return userManagerService.selectPermissionByRolename(rolename);
    }

    @PostMapping("/changeuserrole")
    public Boolean changeUserRole(@RequestBody ChangeRoleDto changeRoleDto){
        return userManagerService.changeRole(changeRoleDto.getUsername(),changeRoleDto.getChangeMap());
    }

    @PostMapping("/changerolepermission")
    public Boolean changeRolePermission(@RequestBody ChangePermissionDto changePermissionDto){
        Map<Long,Boolean> changeMap = new HashMap<>();
        //先将前端传回来的Map<str,bool>转换成Map<long,bool>,便于后续针对id的操作
        for (Map.Entry<String,Boolean> entry : changePermissionDto.getChangeMap().entrySet()){
            changeMap.put(Long.parseLong(entry.getKey()),entry.getValue());
        }
        return userManagerService.changePermission(changePermissionDto.getRolename(),changeMap);
    }

    //指的是把这个role全部删掉，从所有关于role的table中消失
    @RequestMapping("/deleterole")
    public Boolean deleteRole(@RequestParam("rolename") String rolename){
        return userManagerService.deleteRole(rolename);
    }

    @RequestMapping("/close")
    public Boolean close(@RequestParam("username") String username){
        return userManagerService.closeUser(username);
    }

}
