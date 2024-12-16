package com.example.testpowmanage.common.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ChangeAcquisitionTypeDto {
    private Long equipTypeId;//设备类型id
    private Map<String,Boolean> acquisitionTypeMap;//采集类型id - t/f？
}
