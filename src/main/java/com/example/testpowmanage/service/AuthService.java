package com.example.testpowmanage.service;

import com.example.testpowmanage.common.dto.LoginDto;
import com.example.testpowmanage.common.vo.JwtAuthVO;
import org.springframework.stereotype.Service;

public interface AuthService {
    public JwtAuthVO login(LoginDto loginDto);
}
