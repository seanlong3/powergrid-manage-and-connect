package com.example.testpowmanage.common.dto;

import lombok.Data;

@Data
public class RepairFacilityMaintenanceDto {
    private String manager;
    private Integer status;
    private String content;
    private String equipId;
    private Integer hasRenewalPart;
    private Integer isWorking;
}
