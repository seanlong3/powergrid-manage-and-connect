package com.example.testpowmanage.controller;

import com.example.testpowmanage.common.dto.AreaDto;
import com.example.testpowmanage.entity.PowerGridArea;
import com.example.testpowmanage.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/area")
public class AreaController {

    private final AreaService areaService;
    @Autowired
    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping("/listarea")
    public List<PowerGridArea> listArea(){
        return areaService.selectAllArea();
    }

    @RequestMapping("/addarea")
    public Boolean addArea(@RequestBody AreaDto areaDto){
        return areaService.addArea(areaDto);
    }

    @RequestMapping("/deletearea")
    public Boolean deleteArea(@RequestParam("areaid") Long areaId){
        return areaService.deleteAreaById(areaId);
    }



}
