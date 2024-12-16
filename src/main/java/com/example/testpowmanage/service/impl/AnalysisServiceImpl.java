package com.example.testpowmanage.service.impl;

import com.example.testpowmanage.common.dto.EquipDto;
import com.example.testpowmanage.repository.EquipTypeRepository;
import com.example.testpowmanage.entity.EquipType;
import com.example.testpowmanage.service.AnalysisService;
import com.example.testpowmanage.service.EquipService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class AnalysisServiceImpl implements AnalysisService {
    private final EquipTypeRepository equipTypeRepository;

    private final EquipService equipService;

    @Autowired
    public AnalysisServiceImpl(EquipTypeRepository equipTypeRepository, EquipService equipService) {
        this.equipTypeRepository = equipTypeRepository;
        this.equipService = equipService;
    }

    @Override
    public Boolean analysisDataFile(Map<String, List<EquipDto>> dataMap, Long areaId) {
        for (Map.Entry<String, List<EquipDto>> entry : dataMap.entrySet()){
            if (entry.getValue().isEmpty()){
                continue;
            }
            boolean typeInit = equipTypeRepository.selectEquipTypeByTypeName(entry.getKey()) != null;
            for (EquipDto equipDto : entry.getValue()){
                if (!typeInit){
                    Map<String, String> equipTypeData = new HashMap<>();
                    for (Map.Entry<String,Object> objectEntry : equipDto.getData().entrySet()){
                        if (objectEntry.getValue() instanceof  String){
                            equipTypeData.put(objectEntry.getKey(), "varchar");
                        }else if (objectEntry.getValue() instanceof  Integer){
                            equipTypeData.put(objectEntry.getKey(), "int");
                        }else if (objectEntry.getValue() instanceof  Float){
                            equipTypeData.put(objectEntry.getKey(),"float");
                        }else if (objectEntry.getValue() instanceof  Double){
                            equipTypeData.put(objectEntry.getKey(),"float");
                        }else {
                                log.error("No type in {}",objectEntry.getKey());
                        }
                    }
                    equipTypeRepository.insertEquipType(entry.getKey(),equipTypeData);
                    typeInit = true;
                }
                EquipType equipType = equipTypeRepository.selectEquipTypeByTypeName(entry.getKey());
                equipService.addEquip(
                        equipType.getTypeId(),
                        equipDto.getModelId(),
                        equipDto.getEquipName(),
                        areaId,
                        equipDto.getData()
                );
            }
        }
        return true;
    }

}
