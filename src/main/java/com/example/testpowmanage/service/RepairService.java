package com.example.testpowmanage.service;

import com.example.testpowmanage.common.dto.RepairFacilityMaintenanceDto;
import com.example.testpowmanage.common.dto.RepairRoutineInspectionDto;
import com.example.testpowmanage.common.dto.RepairStatusDto;
import com.example.testpowmanage.common.vo.EquipRepairVO;

import java.util.List;

public interface RepairService {

    List<EquipRepairVO> listAllRepair();

    Boolean addRepairFacilityMaintenance(RepairFacilityMaintenanceDto repairDto);

    Boolean addRepairRoutineInspection(RepairRoutineInspectionDto repairDto);

    Boolean changeRepairStatus(String repairId, Integer status);
}
