package com.example.testpowmanage.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcquisitionEquipVO {
    private String acquisitionEquipId;
    private String equipId;
    private String equipName;
    private String modelId;
}
