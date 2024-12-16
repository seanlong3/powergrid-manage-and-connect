package com.example.testpowmanage.controller;

import com.example.testpowmanage.common.dto.*;
import com.example.testpowmanage.common.vo.*;
import com.example.testpowmanage.entity.AcquisitionType;
import com.example.testpowmanage.service.AcquisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


//acquisition成为采集，采集点！
@RestController
@RequestMapping("/acquisition")
public class AcquisitionController {
    private final AcquisitionService acquisitionService;

    @Autowired
    public AcquisitionController(AcquisitionService acquisitionService) {
        this.acquisitionService = acquisitionService;
    }

    //貌似返回对象里的Map参数表没用到？？？？？？
    //获取采集点种类及其详细信息
    @GetMapping("/listallacquisitiontypes")
    public List<AcquisitionTypeVO> listAllAcquisitionTypes() {
        return acquisitionService.listAllAcquisitonType();
    }

    //获取设备类型拥有的采集点类型数据，不包含详细参数
    @GetMapping("/getacquisitiontype")
    public List<AcquisitionType> getAcquisitionType(@RequestParam("equiptypeid") Long equipTypeId) {
        return acquisitionService.getAcquisitionTypeByEquipTypeId(equipTypeId);
    }


    //根据设备类型id获取包含的采集点数据，包含每个采集点类型的详细参数列表
    @PostMapping("/getequiptypeacquisitiondetail")
    public EquipTypeAcquisitionVO getEquipTypeAcquisitionDetail(@RequestParam("equiptypeid") Long equipTypeId) {
        return acquisitionService.getEquipTypeHasAcquisitionByEquipTypeId(equipTypeId);
    }

    //获取设备采集点数据:根据设备id获取采集的时序数据（必须填写开始和结束时间）
    @PostMapping("/getequipdata")
    public EquipAcquisitionDataVO getEquipData(@RequestBody EquipAcquisitionDataByTimeDto dataByTimeDto) {
        return acquisitionService.getEquipAcquisitionData(
                dataByTimeDto.getEquipId(),
                dataByTimeDto.getStartTime(),
                dataByTimeDto.getEndTime()
        );
    }

    //下面连着三个给的api没用到,当练习了
    //根据采集点id获取数据
    @PostMapping("/getAcquisitionEquipDataByAcquisitionEquipId")
    public List<Map<String, Object>> getAcquisitionEquipDataByAcquisitionEquipId(
            @RequestBody AcquisitionEquipIdWithConditionDto dto) {
        return acquisitionService.getAcquisitionDataByAcquisitionEquipId(
                dto.getAcquisitionEquipId(),
                dto.getStartTime(),
                dto.getEndTime()
        );
    }

    //根据采集点种类id获取采集点设备数据
    @GetMapping("/getacquisitionequip")
    public List<AcquisitionEquipVO> getAcquisitionEquip(
            @RequestParam("acquisitiontypeid") Long acquisitionTypeId
    ) {
        return acquisitionService.getEquipAcquisitionEquipsByAcquisitionTypeId(acquisitionTypeId);
    }

    //根据设备类型id获取采集点种类对应关系
    @GetMapping("/getacquisitiontypebyequiptypeid")
    public List<AcquisitionTypeCastEquipTypeVO> getAcquisitionTypeByEquipTypeId(@RequestParam("equiptypeid") Long equipTypeId) {
        return acquisitionService.getAllAcquisitionTypeCastData(equipTypeId);
    }


    @PostMapping("/createacquisitiontype")
    public Boolean createAcquisitionType(@RequestBody AcquisitionTypeDto acquisitionTypeDto) {
        return acquisitionService.createAcquisitionType(
                acquisitionTypeDto.getAcquisitionTypeName(),
                acquisitionTypeDto.getDataMap()
        );
    }

    @PostMapping("/createacquisitionequip")
    public EquipTypeSingleVO createAcquisitionEquip(@RequestBody AcquisitionEquipDto acquisitionEquipDto) {
        return acquisitionService.createAcquisitionEquip(
                acquisitionEquipDto.getEquipId(),
                acquisitionEquipDto.getAcquisitionMap()
        );
    }

    @PostMapping("/changeacquisitiontype")
    public Boolean changeAcquisitionType(@RequestBody ChangeAcquisitionTypeDto changeAcquisitionTypeDto) {
        Map<Long, Boolean> translateMap = new HashMap<>();
        for (Map.Entry<String, Boolean> entry : changeAcquisitionTypeDto.getAcquisitionTypeMap().entrySet()) {
            translateMap.put(Long.parseLong(entry.getKey()), entry.getValue());
        }
        return acquisitionService.changeAcquisitionTypeToEquipType(
                changeAcquisitionTypeDto.getEquipTypeId(),
                translateMap
        );
    }

    @GetMapping("/deleteacquisitionequip")
    public Boolean deleteAcquisitionEquip(@RequestParam("acquisitionequipid") String acquisitionEquipId) {
        return acquisitionService.deleteAcquisitionEquip(acquisitionEquipId);
    }

    @GetMapping("/deleteacquisitiontype")
    public Boolean deleteAcquisitionType(@RequestParam("acquisitiontypeid") Long acquisitionTypeId){
        return acquisitionService.deleteAcquisitionType(acquisitionTypeId);
    }
}
