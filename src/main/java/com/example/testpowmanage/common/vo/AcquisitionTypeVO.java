package com.example.testpowmanage.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcquisitionTypeVO {
    private Long acquisitionId;
    private String acquisitionName;
    private Map<String,String> acquisitionParams;
}
