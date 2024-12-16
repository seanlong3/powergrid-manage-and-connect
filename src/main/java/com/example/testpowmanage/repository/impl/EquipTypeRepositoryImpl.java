package com.example.testpowmanage.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.testpowmanage.repository.EquipTypeRepository;
import com.example.testpowmanage.entity.ColumnInfo;
import com.example.testpowmanage.entity.EquipType;
import com.example.testpowmanage.mapper.EquipTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class EquipTypeRepositoryImpl implements EquipTypeRepository {
    private final EquipTypeMapper equipTypeMapper;

    @Autowired
    public EquipTypeRepositoryImpl(EquipTypeMapper equipTypeMapper) {
        this.equipTypeMapper = equipTypeMapper;
    }

    @Override
    public List<EquipType> selectAllEquipType() {
        return equipTypeMapper.selectList(null);
    }


    @Override
    //这个东西就是找到自定义名称，及其数据类型，并扔到一个map里组成key-value
    public Map<String, String> selectEquipTypeData(Long equipTypeId) {
        String equipTableName = equipTypeMapper.selectById(equipTypeId).getEquipTbName();
        List<ColumnInfo> columnInfos = equipTypeMapper.getColumnInfo(equipTableName);

        Map<String, String> result = new HashMap<>();
        //排除一部分现有数据
        final Set<String> excludeList = Set.of("equip_id", "type_id", "equip_name", "model_id", "area_id");
        columnInfos.forEach(e -> {
            if (!excludeList.contains(e.getColumnName())) {
                result.put(e.getColumnName(), e.getDataType());
            }
        });
        return result;
    }
    @Override
    public EquipType selectEquipTypeById(Long equipTypeId) {
        return equipTypeMapper.selectById(equipTypeId);
    }

    @Override
    public Boolean deleteEquipType(Long equipTypeId) {
        EquipType equipType = equipTypeMapper.selectById(equipTypeId);
        if (equipType==null){
            return null;//???????
        }
        equipTypeMapper.deleteEquipTable(equipType.getEquipTbName());
        equipTypeMapper.deleteById(equipTypeId);
        return true;
    }


    @Override
    public EquipType selectEquipTypeByTypeName(String typename) {
        return equipTypeMapper.selectOne(new QueryWrapper<EquipType>().eq("type_name",typename));
    }


    @Override
    public void insertEquipType(String typeName, Map<String, String> columns) {
        if (equipTypeMapper.exists(new QueryWrapper<EquipType>().eq("type_name",typeName))){
            return;
        }
        String tableName = "equip_" + UUID.randomUUID().toString().replace('-','_');
        EquipType equipType = new EquipType(null,typeName,tableName);
        equipTypeMapper.insert(equipType);
        equipTypeMapper.createEquipTable(equipType.getEquipTbName(),columns);
    }
}
