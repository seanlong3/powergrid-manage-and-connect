package com.example.testpowmanage.repository;

import com.example.testpowmanage.entity.Equip;

import java.util.List;
import java.util.Map;

public interface EquipRepository {
    List<Equip> selectAllEquip(String tableName, Long areaId);

    Boolean deleteEquip(String equipId, Long equipTypeId);

    void insertEquipType(String typeName, Map<String, String> columns);

    void insertEquip(String equipTbName, Map<String, Object> data);
}
