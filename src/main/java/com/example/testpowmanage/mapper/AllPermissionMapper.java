package com.example.testpowmanage.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.testpowmanage.entity.Permission;

@DS("opengauss")
public interface AllPermissionMapper extends BaseMapper<Permission> {
}
