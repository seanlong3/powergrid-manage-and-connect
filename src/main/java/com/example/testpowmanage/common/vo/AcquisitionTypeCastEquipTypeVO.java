package com.example.testpowmanage.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcquisitionTypeCastEquipTypeVO {
    private Long acquisitionTypeId;
    private String acquisitionTypeName;
    private Boolean isUsed;
}
