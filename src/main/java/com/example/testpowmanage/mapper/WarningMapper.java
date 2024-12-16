package com.example.testpowmanage.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.testpowmanage.common.vo.WarningVO;
import com.example.testpowmanage.entity.Warning;

import java.util.List;

@DS("opengauss")
public interface WarningMapper extends BaseMapper<Warning> {
    List<WarningVO> selectAllWarningWithEquip();


}
