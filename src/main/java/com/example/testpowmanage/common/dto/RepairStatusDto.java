package com.example.testpowmanage.common.dto;

import com.google.common.annotations.VisibleForTesting;
import lombok.Data;

@Data
public class RepairStatusDto {
    private String repairId;
    private Integer status;
}
