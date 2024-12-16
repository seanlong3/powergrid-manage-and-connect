package com.example.testpowmanage.common.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ChangePermissionDto {
    private String rolename;
    private Map<String, Boolean> changeMap; //"permissionid" : T/F
}
