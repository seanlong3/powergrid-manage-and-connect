package com.example.testpowmanage.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.testpowmanage.entity.RolePermission;

import java.util.List;

@DS("opengauss")
public interface AllRolePermissionMapper extends BaseMapper<RolePermission> {
    List<Long> findRoleIdByPermissionId(Long permissionId);
}
