package com.example.testpowmanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.testpowmanage.common.vo.WarningVO;
import com.example.testpowmanage.entity.Warning;
import com.example.testpowmanage.mapper.WarningMapper;
import com.example.testpowmanage.service.WarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class WarningServiceImpl implements WarningService {
    private final WarningMapper warningMapper;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public WarningServiceImpl(WarningMapper warningMapper) {
        this.warningMapper = warningMapper;
    }
    @Override
    public List<WarningVO> getAllWarning() {
        return warningMapper.selectAllWarningWithEquip();
    }

    @Override
    public List<Warning> getWarning(String equipId, String startTime, String endTime) throws ParseException {
        QueryWrapper<Warning> wrapper = new QueryWrapper<Warning>()
                .eq("equip_id",equipId)
                .eq("is_deleted",0);
        if (startTime != null && !startTime.isBlank()){
            wrapper.ge("warning_time",simpleDateFormat.parse(startTime));
        }
        if (endTime!=null && !endTime.isBlank()){
            wrapper.le("warning_time",simpleDateFormat.parse(endTime));
        }

        return warningMapper.selectList(wrapper);
    }

    @Override
    public Boolean solveWarning(Long warningId) {
        Warning warning = warningMapper.selectById(warningId);
        if (warning== null || warning.getIsSolved()==1){
            return false;
        }
        warning.setIsSolved(1);
        warningMapper.updateById(warning);
        return true;
    }

    @Override
    public Boolean deleteWarning(Long warningId) {
        Warning warning = warningMapper.selectById(warningId);
        if (warning == null || warning.getIsDeleted()==1){
            return false;
        }
        warning.setIsDeleted(1);
        warningMapper.updateById(warning);

        return true;
    }
}
