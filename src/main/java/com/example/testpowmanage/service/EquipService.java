package com.example.testpowmanage.service;

import com.example.testpowmanage.common.dto.EquipTypeDto;
import com.example.testpowmanage.entity.Equip;

import java.util.List;
import java.util.Map;

public interface EquipService {
    List<EquipTypeDto> listAllEquipType();

    List<Equip> listAllEquip(Long areaId);

    EquipTypeDto getEquipTypeModel(Long equipTypeId);

    Boolean createEquipType(String typeName, Map<String, String> columns);

    Boolean addEquip(Long typeId, String modelId, String equipName, Long areaId, Map<String, Object> values);

    Boolean deleteEquip(String equipId, Long equipTypeId);

    Boolean deleteEquipType(Long equipTypeId);
}
