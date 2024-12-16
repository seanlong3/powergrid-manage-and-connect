package com.example.testpowmanage.common.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipAcquisitionDataVO {
    private String equipId;
    private List<AcquisitionMetaData> acquisitionMetaDataList;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AcquisitionMetaData{
        public String acquisitionEquipId;
        public String acquisitionTypeName;
        public List<Map<String, Object>> dataMap;
    }
}
