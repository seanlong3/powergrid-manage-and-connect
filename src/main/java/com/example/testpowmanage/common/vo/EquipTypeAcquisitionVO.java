package com.example.testpowmanage.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipTypeAcquisitionVO {
    private String equipTypeName;
    private List<AcquisitionMetadata> acquisitionMetadata;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AcquisitionMetadata{
        public Long acquisitionTypeId;
        public String acquisitionTypeName;
        public Map<String,String> paramMap;
    }
}
