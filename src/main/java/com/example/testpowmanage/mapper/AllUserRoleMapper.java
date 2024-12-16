package com.example.testpowmanage.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.testpowmanage.entity.Role;
import com.example.testpowmanage.entity.UserRole;

@DS("opengauss")
public interface AllUserRoleMapper extends BaseMapper<UserRole> {
}
