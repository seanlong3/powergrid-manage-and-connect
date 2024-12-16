package com.example.testpowmanage.common.dto;

import lombok.Data;

import java.util.Map;
@Data
public class EquipDto {
    private Long typeId;
    private String equipName;
    private String modelId;
    private Long areaId;
    private Map<String,Object> data;
}
