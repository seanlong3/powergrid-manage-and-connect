package com.example.testpowmanage.service;

import com.example.testpowmanage.common.vo.*;
import com.example.testpowmanage.entity.AcquisitionType;

import java.util.List;
import java.util.Map;

public interface AcquisitionService {
    List<AcquisitionTypeVO> listAllAcquisitonType();

    EquipTypeAcquisitionVO getEquipTypeHasAcquisitionByEquipTypeId(Long equipTypeId);

    List<AcquisitionType> getAcquisitionTypeByEquipTypeId(Long equipTypeId);

    EquipAcquisitionDataVO getEquipAcquisitionData(String equipId, String startTime, String endTime);

    List<Map<String, Object>> getAcquisitionDataByAcquisitionEquipId(String acquisitionEquipId, String startTime, String endTime);

    List<AcquisitionEquipVO> getEquipAcquisitionEquipsByAcquisitionTypeId(Long acquisitionTypeId);

    List<AcquisitionTypeCastEquipTypeVO> getAllAcquisitionTypeCastData(Long equipTypeId);

    Boolean createAcquisitionType(String acquisitionTypeName, Map<String, String> dataMap);

    EquipTypeSingleVO createAcquisitionEquip(String equipId, Map<String, Long> acquisitionMap);

    Boolean changeAcquisitionTypeToEquipType(Long equipTypeId, Map<Long, Boolean> translateMap);

    Boolean deleteAcquisitionEquip(String acquisitionEquipId);

    Boolean deleteAcquisitionType(Long acquisitionTypeId);
}
