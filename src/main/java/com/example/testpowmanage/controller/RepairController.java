package com.example.testpowmanage.controller;

import com.example.testpowmanage.common.dto.RepairFacilityMaintenanceDto;
import com.example.testpowmanage.common.dto.RepairRoutineInspectionDto;
import com.example.testpowmanage.common.dto.RepairStatusDto;
import com.example.testpowmanage.common.vo.EquipRepairVO;
import com.example.testpowmanage.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair")
public class RepairController {
    private final RepairService repairService;

    @Autowired
    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    //获取所有检修数据
    @GetMapping("/getrepairs")
    public List<EquipRepairVO> getRepairs(){
        return repairService.listAllRepair();
    }

    //添加设备检修信息
    @PostMapping("/addrepairfacilitymaintenance")
    public Boolean addRepairFacilityMaintenance(@RequestBody RepairFacilityMaintenanceDto repairDto){
        return repairService.addRepairFacilityMaintenance(repairDto);
    }

    //添加巡检信息
    @PostMapping("/addrepairroutineinspection")
    public Boolean addRepairRoutineInspection(@RequestBody RepairRoutineInspectionDto repairDto){
        return repairService.addRepairRoutineInspection(repairDto);
    }

    //设置检修状态
    @PostMapping("/setrepairstatus")
    public Boolean setRepairStatus(@RequestBody RepairStatusDto repairStatusDto){
        return repairService.changeRepairStatus(repairStatusDto.getRepairId(),repairStatusDto.getStatus());
    }
}
