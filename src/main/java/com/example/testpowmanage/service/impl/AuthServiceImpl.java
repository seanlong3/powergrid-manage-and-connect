package com.example.testpowmanage.service.impl;

import com.example.testpowmanage.common.dto.LoginDto;
import com.example.testpowmanage.common.vo.JwtAuthVO;
import com.example.testpowmanage.entity.Role;
import com.example.testpowmanage.service.AuthService;
import com.example.testpowmanage.service.UserManagerService;
import com.example.testpowmanage.utli.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserManagerService userManagerService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserManagerService userManagerService,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userManagerService = userManagerService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public JwtAuthVO login(LoginDto loginDto) {
        //构造登录验证信息，并且进行认证
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),loginDto.getPassword()
        ));
        //将认证信息存在sch中
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //查找该用户名对应的roleList
        List<String> roleNameList = userManagerService.findRoleByUsername(loginDto.getUsername())
                .stream().map(Role::getRoleName).toList();
        //返回令牌
        return new JwtAuthVO(jwtTokenProvider.generateToken(authentication),roleNameList);
    }

}
