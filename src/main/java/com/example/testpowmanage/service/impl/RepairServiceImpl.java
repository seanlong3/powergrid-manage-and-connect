package com.example.testpowmanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.testpowmanage.common.dto.RepairFacilityMaintenanceDto;
import com.example.testpowmanage.common.dto.RepairRoutineInspectionDto;
import com.example.testpowmanage.common.vo.EquipRepairVO;
import com.example.testpowmanage.entity.EquipCastEquipType;
import com.example.testpowmanage.entity.EquipRepair;
import com.example.testpowmanage.entity.RepairFacilityMaintenance;
import com.example.testpowmanage.entity.RepairRoutineInspection;
import com.example.testpowmanage.mapper.EquipCastEquipTypeMapper;
import com.example.testpowmanage.mapper.EquipRepairMapper;
import com.example.testpowmanage.mapper.RepairFacilityMaintenanceMapper;
import com.example.testpowmanage.mapper.RepairRoutineInspectionMapper;
import com.example.testpowmanage.service.RepairService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
public class RepairServiceImpl implements RepairService {

    private final EquipRepairMapper equipRepairMapper;
    private final EquipCastEquipTypeMapper equipCastEquipTypeMapper;
    private final RepairFacilityMaintenanceMapper repairFacilityMaintenanceMapper;
    private final RepairRoutineInspectionMapper repairRoutineInspectionMapper;

    @Autowired
    public RepairServiceImpl(EquipRepairMapper equipRepairMapper, EquipCastEquipTypeMapper equipCastEquipTypeMapper, RepairFacilityMaintenanceMapper repairFacilityMaintenanceMapper, RepairRoutineInspectionMapper repairRoutineInspectionMapper) {
        this.equipRepairMapper = equipRepairMapper;
        this.equipCastEquipTypeMapper = equipCastEquipTypeMapper;
        this.repairFacilityMaintenanceMapper = repairFacilityMaintenanceMapper;
        this.repairRoutineInspectionMapper = repairRoutineInspectionMapper;
    }

    @Override
    public List<EquipRepairVO> listAllRepair() {
        return equipRepairMapper.selectRepairWithAllType();
    }

    @Override
    @Transactional
    public Boolean addRepairFacilityMaintenance(RepairFacilityMaintenanceDto repairDto) {
        LocalDateTime currentTime = LocalDateTime.now();
        EquipRepair equipRepair = new EquipRepair(
                null,
                currentTime,
                repairDto.getManager(),
                0,
                repairDto.getContent(),
                0
        );
        equipRepairMapper.insert(equipRepair);
        EquipCastEquipType equipCastEquipType = equipCastEquipTypeMapper.selectOne(
                new QueryWrapper<EquipCastEquipType>().eq("equip_id",repairDto.getEquipId())
        );
        RepairFacilityMaintenance repairFacilityMaintenance = RepairFacilityMaintenance.builder()
                .repairId(equipRepair.getRepairId())
                .equipId(repairDto.getEquipId())
                .equipName(equipCastEquipType.getEquipName())
                .hasRenewalPart(repairDto.getHasRenewalPart())
                .isWorking(repairDto.getIsWorking())
                .modelId(equipCastEquipType.getModelId()).build();
        repairFacilityMaintenanceMapper.insert(repairFacilityMaintenance);
        return true;
    }


    @Override
    public Boolean addRepairRoutineInspection(RepairRoutineInspectionDto repairDto) {
        LocalDateTime currentTime = LocalDateTime.now();
        EquipRepair equipRepair = new EquipRepair(
                null,
                currentTime,
                repairDto.getManager(),
                0,
                repairDto.getContent(),
                1
        );
        equipRepairMapper.insert(equipRepair);

        RepairRoutineInspection repairRoutineInspection = RepairRoutineInspection.builder()
                .repairId(equipRepair.getRepairId())
                .areaId(repairDto.getAreaId())
                .routeRecord(repairDto.getRouteRecord()).build();
        repairRoutineInspectionMapper.insert(repairRoutineInspection);
        return true;
    }

    @Override
    public Boolean changeRepairStatus(String repairId, Integer status) {
        UpdateWrapper<EquipRepair> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("repair_id",repairId)
                     .set("status",status);
        equipRepairMapper.update(null,updateWrapper);
        return true;
    }
}
