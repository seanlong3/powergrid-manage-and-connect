package com.example.testpowmanage.controller;

import com.example.testpowmanage.common.dto.DeleteEquipDto;
import com.example.testpowmanage.common.dto.EquipDto;
import com.example.testpowmanage.common.dto.EquipTypeDto;
import com.example.testpowmanage.entity.Equip;
import com.example.testpowmanage.service.EquipService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equip")
@Log4j2
public class EquipController {
    private final EquipService equipService;

    @Autowired
    public EquipController(EquipService equipService) {
        this.equipService = equipService;
    }

    @GetMapping("/getequiptypes")
    public List<EquipTypeDto> getEquipTypes(){
        return equipService.listAllEquipType();//返回类型和api实例质疑
    }

    @GetMapping("/getequips")
    public List<Equip> getEquips(@RequestParam("areaid") Long areaId){
        return equipService.listAllEquip(areaId);
    }

    //获取设备种类具体数据要求，即参数名称加数据类型
    @GetMapping("/checkequiptype")
    public EquipTypeDto checkEquipType(@RequestParam("equiptypeid") Long equipTypeId){
        return equipService.getEquipTypeModel(equipTypeId);
    }

    @RequestMapping("/addequiptype")
    public Boolean addEquipType(@RequestBody EquipTypeDto equipTypeDto){
        return equipService.createEquipType(equipTypeDto.getTypeName(),equipTypeDto.getData());
    }

    @RequestMapping("/addequip")
    public Boolean addEquip(@RequestBody EquipDto equipDto){
        return equipService.addEquip(
                equipDto.getTypeId(),
                equipDto.getModelId(),
                equipDto.getEquipName(),
                equipDto.getAreaId(),
                equipDto.getData()
        );
    }
    @RequestMapping("/deleteequip")
    public Boolean deleteEquip(@RequestBody DeleteEquipDto deleteEquipDto){
        return equipService.deleteEquip(deleteEquipDto.getEquipId(),deleteEquipDto.getEquipTypeId());
    }

    @RequestMapping("/deleteequiptype")
    public Boolean deleteEquipType(@RequestParam("equiptypeid") Long equipTypeId){
        return equipService.deleteEquipType(equipTypeId);
    }

    //貌似扔掉了一个接口废案
}
