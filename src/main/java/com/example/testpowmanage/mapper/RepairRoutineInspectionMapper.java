package com.example.testpowmanage.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.testpowmanage.entity.RepairRoutineInspection;

@DS("opengauss")
public interface RepairRoutineInspectionMapper extends BaseMapper<RepairRoutineInspection> {
}
