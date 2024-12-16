package com.example.testpowmanage.repository;

import com.example.testpowmanage.entity.EquipType;

import java.util.List;
import java.util.Map;

public interface EquipTypeRepository {
    List<EquipType> selectAllEquipType();

    Map<String, String> selectEquipTypeData(Long equipTypeId);

    EquipType selectEquipTypeById(Long equipTypeId);

    Boolean deleteEquipType(Long equipTypeId);

    EquipType selectEquipTypeByTypeName(String typename);

    void insertEquipType(String typeName, Map<String, String> columns);
}
