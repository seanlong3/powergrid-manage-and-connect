package com.example.testpowmanage.service;

import com.example.testpowmanage.common.vo.WarningVO;
import com.example.testpowmanage.entity.Warning;

import java.text.ParseException;
import java.util.List;

public interface WarningService {
    List<WarningVO> getAllWarning();

    List<Warning> getWarning(String equipId, String startTime, String endTime) throws ParseException;

    Boolean solveWarning(Long warningId);

    Boolean deleteWarning(Long warningId);
}
