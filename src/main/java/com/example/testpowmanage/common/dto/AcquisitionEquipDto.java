package com.example.testpowmanage.common.dto;

import lombok.Data;

import java.util.Map;

@Data
public class AcquisitionEquipDto {
    private String equipId;
    private Map<String, Long> acquisitionMap;//采集设备id -》采集种类id
}
