package com.example.testpowmanage.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.testpowmanage.common.vo.EquipRepairVO;
import com.example.testpowmanage.entity.EquipRepair;

import java.util.List;

@DS("opengauss")
public interface EquipRepairMapper extends BaseMapper<EquipRepair> {
    List<EquipRepairVO> selectRepairWithAllType();
}
