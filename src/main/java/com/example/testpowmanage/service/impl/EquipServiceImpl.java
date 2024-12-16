package com.example.testpowmanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.testpowmanage.common.dto.EquipTypeDto;
import com.example.testpowmanage.repository.EquipRepository;
import com.example.testpowmanage.repository.EquipTypeRepository;
import com.example.testpowmanage.entity.*;
import com.example.testpowmanage.mapper.AcquisitionToEquipTypeMapper;
import com.example.testpowmanage.mapper.EquipCastEquipTypeMapper;
import com.example.testpowmanage.mapper.PowerGridAreaMapper;
import com.example.testpowmanage.service.EquipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EquipServiceImpl implements EquipService {
    private final EquipTypeRepository equipTypeRepository;
    private final EquipRepository equipRepository;
    private final PowerGridAreaMapper powerGridAreaMapper;
    private final EquipCastEquipTypeMapper equipCastEquipTypeMapper;
    private final AcquisitionToEquipTypeMapper acquisitionToEquipTypeMapper;
    @Autowired
    public EquipServiceImpl(EquipTypeRepository equipTypeRepository, EquipRepository equipRepository, PowerGridAreaMapper powerGridAreaMapper, EquipCastEquipTypeMapper equipCastEquipTypeMapper, AcquisitionToEquipTypeMapper acquisitionToEquipTypeMapper) {
        this.equipTypeRepository = equipTypeRepository;
        this.equipRepository = equipRepository;
        this.powerGridAreaMapper = powerGridAreaMapper;
        this.equipCastEquipTypeMapper = equipCastEquipTypeMapper;
        this.acquisitionToEquipTypeMapper = acquisitionToEquipTypeMapper;
    }

    @Override
    public List<EquipTypeDto> listAllEquipType() {
        List<EquipType> equipTypeList = equipTypeRepository.selectAllEquipType();
        List<EquipTypeDto> equipTypeDtoList = new ArrayList<>();
        for (EquipType equipType: equipTypeList){
            equipTypeDtoList.add(new EquipTypeDto(
                    equipType.getTypeId(),
                    equipType.getTypeName(),
                    equipTypeRepository.selectEquipTypeData(equipType.getTypeId())
            ));
        }
        return equipTypeDtoList;
    }
    @Override
    public List<Equip> listAllEquip(Long areaId) {
        //先找到equiptype的数据，再从equiptype中拿到equip table name从而可以调用selectAllEquip方法拿到List<Equip>
        List<EquipType> types = equipTypeRepository.selectAllEquipType();
        List<Equip> result = new ArrayList<>();
        for (EquipType type:types){
            result.addAll(equipRepository.selectAllEquip(type.getEquipTbName(),areaId));
        }
        return result;
    }

    @Override
    public EquipTypeDto getEquipTypeModel(Long equipTypeId) {
        EquipType equipType = equipTypeRepository.selectEquipTypeById(equipTypeId);
        return new EquipTypeDto(
                equipType.getTypeId(),
                equipType.getTypeName(),
                equipTypeRepository.selectEquipTypeData(equipTypeId));
    }

    @Override
    public Boolean createEquipType(String typeName, Map<String, String> columns) {
        if (columns.isEmpty()){
            return false;
        }
        equipRepository.insertEquipType(typeName,columns);
        return true;
    }

    @Override
    public Boolean addEquip(Long typeId, String modelId, String equipName, Long areaId, Map<String, Object> values) {
        EquipType typeResult = equipTypeRepository.selectEquipTypeById(typeId);
        if (typeResult==null){
            return false;
        }
        Set<String> existEquipIds = listAllEquip(areaId).stream()
                .map(Equip::getEquipId).collect(Collectors.toSet());//特别方法
        String equipId = UUID.randomUUID().toString();
        while (existEquipIds.contains(equipId)){
            equipId=UUID.randomUUID().toString();
        }
        values.putAll(Map.of(
                "equip_id",equipId,
                "type_id",typeId,
                "equip_name",equipName,
                "model_id",modelId,
                "area_id",areaId
        ));
        //equip对应的tablename中插入
        equipRepository.insertEquip(typeResult.getEquipTbName(),values);
        //equip当然还对应一个equipType，其关系表cast也要进行插入
        equipCastEquipTypeMapper.insert(EquipCastEquipType.builder()
                .equipId(equipId)
                .equipName(equipName)
                .modelId(modelId)
                .equipTypeId(typeId)
                .build());
        return true;
    }

    @Override
    public Boolean deleteEquip(String equipId, Long equipTypeId) {
        return equipRepository.deleteEquip(equipId,equipTypeId);
    }

    @Override
    public Boolean deleteEquipType(Long equipTypeId) {
        EquipType equipType = equipTypeRepository.selectEquipTypeById(equipTypeId);
        if (equipType==null){
            return false;
        }
        List<PowerGridArea> areas = powerGridAreaMapper.selectList(null);
        for (PowerGridArea area: areas){
            List<String> equipList = equipRepository
                    .selectAllEquip(equipType.getEquipTbName(),area.getAreaId()).stream()
                    .map(Equip::getEquipId).toList();
            for (String equipId : equipList){
                equipRepository.deleteEquip(equipId,equipTypeId);
            }
            List<Long> acquisitionToEquipTypeIds =
                    acquisitionToEquipTypeMapper.selectList(
                            new QueryWrapper<AcquisitionToEquipType>().eq("type_id",equipTypeId)
                                    ).stream().map(AcquisitionToEquipType::getId).toList();
            if (!acquisitionToEquipTypeIds.isEmpty()){
                acquisitionToEquipTypeMapper.deleteByIds(acquisitionToEquipTypeIds);
            }
        }
        return equipTypeRepository.deleteEquipType(equipTypeId);
    }
}
