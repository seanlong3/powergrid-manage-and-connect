package com.example.testpowmanage.common.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ChangeRoleDto {
    private String username;
    private Map<String,Boolean> changeMap; // "roleid" : T/F
}
