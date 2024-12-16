package com.example.testpowmanage.common.dto;
import lombok.Data;

import java.util.Map;

@Data
public class AcquisitionTypeDto {
    private String acquisitionTypeName;
    private Map<String,String> dataMap;
}
