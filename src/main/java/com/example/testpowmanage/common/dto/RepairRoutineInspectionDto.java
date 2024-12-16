package com.example.testpowmanage.common.dto;

import lombok.Data;

@Data
public class RepairRoutineInspectionDto {
    private String manager;
    private Integer status;
    private String content;

    private Long areaId;
    private String routeRecord;
}
