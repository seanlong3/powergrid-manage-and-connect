package com.example.testpowmanage.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtAuthVO {
    private String token;
    private List<String> roleName;
}
