package com.example.testpowmanage.controller;

import com.example.testpowmanage.common.dto.WarningSearchDto;
import com.example.testpowmanage.common.vo.WarningVO;
import com.example.testpowmanage.entity.Warning;
import com.example.testpowmanage.service.WarningService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/warning")
@Log4j2
public class WarningController {
    private final WarningService warningService;

    @Autowired
    public WarningController(WarningService warningService) {
        this.warningService = warningService;
    }

    @GetMapping("/getallwarninginfo")
    public List<WarningVO> getAllWarningInfo(){
        return warningService.getAllWarning();
    }

    @PostMapping("/getwarninginfo")
    public List<Warning> getWarningInfo(
            @RequestBody WarningSearchDto warningSearchDto) throws ParseException {
        return warningService.getWarning(
                warningSearchDto.getEquipId(),
                warningSearchDto.getStartTime(),
                warningSearchDto.getEndTime());
    }

    @PostMapping("/solvewarning")
    public Boolean solveWarning(@RequestParam("warningid") Long warningId){
        return warningService.solveWarning(warningId);
    }

    @PostMapping("/deletewarning")
    public Boolean deleteWarning(@RequestParam("warningid") Long warningId){
        return warningService.deleteWarning(warningId);
    }
}
