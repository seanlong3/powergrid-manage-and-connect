package com.example.testpowmanage.service;

import com.example.testpowmanage.common.dto.EquipDto;

import java.util.List;
import java.util.Map;

public interface AnalysisService {
    Boolean analysisDataFile(Map<String, List<EquipDto>> dataMap, Long areaId);
}
