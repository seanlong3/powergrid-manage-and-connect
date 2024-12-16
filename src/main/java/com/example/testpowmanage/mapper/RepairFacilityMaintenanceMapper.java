package com.example.testpowmanage.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.testpowmanage.entity.RepairFacilityMaintenance;
@DS("opengauss")
public interface RepairFacilityMaintenanceMapper extends BaseMapper<RepairFacilityMaintenance> {
}
