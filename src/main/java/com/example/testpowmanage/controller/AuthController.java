package com.example.testpowmanage.controller;


import com.example.testpowmanage.common.dto.LoginDto;
import com.example.testpowmanage.common.vo.JwtAuthVO;
import com.example.testpowmanage.service.AuthService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping ("/login")
    public JwtAuthVO login(@RequestBody LoginDto loginDto) {
/*        loginDto.setUsername("admin");
        loginDto.setPassword("123456");*/
        return authService.login(loginDto);
    }
}
