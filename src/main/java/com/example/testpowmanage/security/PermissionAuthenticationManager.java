package com.example.testpowmanage.security;

import com.example.testpowmanage.repository.UserRepository;
import com.example.testpowmanage.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Supplier;

@Component
public class PermissionAuthenticationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final UserRepository userRepository;

    @Autowired
    public PermissionAuthenticationManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Set<String> set = new HashSet<>();
        String requestUrl = object.getRequest().getRequestURI();
        String requestUrlWithPre = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "**";
        List<Permission> permissions = userRepository.selectAllPermission();
        List<Long> permissionIds = new ArrayList<>();

        // check all matched url permission，查找匹配的权限，通过 userRepository.selectAllPermission() 查询数据库中所有的权限记录。
        //遍历所有权限，检查当前请求的 URL 是否与权限的 URL 匹配。支持两种匹配：
        //直接匹配请求 URL 和权限 URL。
        //匹配以 /** 结尾的通配符路径。
        //将所有匹配的权限 ID 存入 permissionIds 列表。
        for (Permission permission : permissions){
            if (permission.getUrl().endsWith("**") && requestUrlWithPre.equals(permission.getUrl())){
                permissionIds.add(permission.getPermissionId());
            } else if (permission.getUrl().equals(requestUrl)) {
                permissionIds.add(permission.getPermissionId());
            }
        }

        // check all have permission role，查找权限对应的角色，并将角色名称存入set集合中，初始默认各种permissionId全是admin
        for (Long perId : permissionIds){
            userRepository.findRolesByPermissionId(perId)
                    .forEach(role -> set.add(role.getRoleName())); //很舒服的操作，没怎么用过
        }

        //验证用户的角色权限
        //获取当前用户的角色信息（即 GrantedAuthority），通过 authentication.get().getAuthorities() 获取用户所有的角色，并将角色名称提取出来。
        Collection<String> authorities = authentication.get().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        //遍历前面匹配到的角色（set 集合），检查用户是否有任何一个匹配的角色。如果有，则返回 AuthorizationDecision(true)，表示授权通过。
        if (!set.isEmpty()) {
            for (String roleName : set) {
                if (authorities.contains(roleName)) {
                    return new AuthorizationDecision(true);
                }
            }
        }
        //如果没有匹配的角色，返回 AuthorizationDecision(false)，表示用户没有权限访问该 URL。
        return new AuthorizationDecision(false);
    }
}
