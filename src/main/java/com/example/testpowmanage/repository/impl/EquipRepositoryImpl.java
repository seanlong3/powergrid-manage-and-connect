package com.example.testpowmanage.repository.impl;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.testpowmanage.config.hander.EquipTableNameHandler;
import com.example.testpowmanage.repository.EquipRepository;
import com.example.testpowmanage.entity.AcquisitionEquipCast;
import com.example.testpowmanage.entity.Equip;
import com.example.testpowmanage.entity.EquipType;
import com.example.testpowmanage.mapper.AcquisitionDataMapper;
import com.example.testpowmanage.mapper.AcquisitionEquipCastMapper;
import com.example.testpowmanage.mapper.EquipMapper;
import com.example.testpowmanage.mapper.EquipTypeMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Log4j2
public class EquipRepositoryImpl implements EquipRepository {
    private final EquipMapper equipMapper;
    private final EquipTypeMapper equipTypeMapper;
    private final AcquisitionEquipCastMapper acquisitionEquipCastMapper;

    private final AcquisitionDataMapper acquisitionDataMapper;

    @Autowired
    public EquipRepositoryImpl(EquipMapper equipMapper, EquipTypeMapper equipTypeMapper, AcquisitionEquipCastMapper acquisitionEquipCastMapper, AcquisitionDataMapper acquisitionDataMapper) {
        this.equipMapper = equipMapper;
        this.equipTypeMapper = equipTypeMapper;
        this.acquisitionEquipCastMapper = acquisitionEquipCastMapper;
        this.acquisitionDataMapper = acquisitionDataMapper;
    }


    @Override
    public List<Equip> selectAllEquip(String tableName, Long areaId) {
        EquipTableNameHandler.setEquipName(tableName);//??跟多线程有关系
        List<Map<String, Object>> resultMapList = equipMapper.selectAllEquipByAreaId(areaId);
        EquipTableNameHandler.removeEquipName();

        List<Equip> result = new ArrayList<>();
        //遍历equip
        for (Map<String, Object> resultMap : resultMapList) {
            Equip equip = new Equip();
            Map<String, Object> dataMap = new HashMap<>();
            //遍历每个equipMap中的每项数据
            for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                //用旧版本switch一样，每项单独加上break即可
                switch (key) {
                    case "equip_id" -> equip.setEquipId((String) value);
                    case "type_id" -> equip.setTypeId((Long) value);
                    case "model_id" -> equip.setModelId((String) value);
                    case "area_id" -> equip.setAreaId((Long) value);
/*                  case null:
                        break;*/
                    default -> dataMap.put(key, value);
                }
            }
            equip.setData(dataMap);//给现有equip补充数据
            result.add(equip);//单个equip完成了设置，扔进这个List里
        }
        return result;
    }

    @Override
    public Boolean deleteEquip(String equipId, Long equipTypeId) {
        //方法参数里给的equipTypeId仅用来找对应的equip的tablename
        EquipType equipType = equipTypeMapper.selectById(equipTypeId);
        if (equipType==null){
            return false;
        }
        EquipTableNameHandler.setEquipName(equipType.getEquipTbName());//设置当前设备的表名，后续数据库操作将使用该表名。
        //找出与该equipId对应的equip并删除
        try {
            if (!equipMapper.exists(new QueryWrapper<Equip>()
                    .eq("equip_id",equipId))){
                return false;
            }
            equipMapper.deleteById(equipId);
        }finally {
            EquipTableNameHandler.removeEquipName();//清理之前设置的设备表名，确保后续操作不受影响。
        }
        //找出acquisitionEquipCast中与equipId有关的数据，放到一个List中
        QueryWrapper<AcquisitionEquipCast> acquisitionEquipCastQueryWrapper =
                new QueryWrapper<AcquisitionEquipCast>().eq("equip_id",equipId);
        if (acquisitionEquipCastMapper.exists(acquisitionEquipCastQueryWrapper)){
            List<AcquisitionEquipCast> acquisitionEquipCastList =
                    acquisitionEquipCastMapper.selectList(acquisitionEquipCastQueryWrapper);
            Set<String> acquisitionEquipIds = new HashSet<>();
            if (acquisitionEquipCastList != null && !acquisitionEquipCastList.isEmpty()){
                DynamicDataSourceContextHolder.push("tdengine");
                try {
                    //遍历这个List，并且通过它删掉对应的childtable，同时拿到它的id存起来，最后进行完全删除
                    for (AcquisitionEquipCast item : acquisitionEquipCastList){
                        acquisitionDataMapper.deleteAcquisitionEquipChildtable(item.getChildTableName());
                        acquisitionEquipIds.add(item.getAcquisitionEquipId());
                    }
                }finally {
                    DynamicDataSourceContextHolder.clear();
                }
                acquisitionEquipCastMapper.deleteByIds(acquisitionEquipIds);
            }
        }
        return true;
    }

    @Override
    @Transactional
    public void insertEquipType(String typeName, Map<String, String> columns) {
        if (equipTypeMapper.exists(new QueryWrapper<EquipType>().eq("type_name",typeName))){
            return;//这里的逻辑处理应该可以优化一下，比如回去之后显示一下有重复名称
        }
        String tableName = "equip_" + UUID.randomUUID().toString().replace('-','_');
        EquipType equipType = new EquipType(null,typeName,tableName);
        equipTypeMapper.insert(equipType);
        equipTypeMapper.createEquipTable(equipType.getEquipTbName(),columns);
    }

    @Override
    public void insertEquip(String equipTbName, Map<String, Object> data) {
        equipMapper.insertEquipData(data,equipTbName);
    }
}
