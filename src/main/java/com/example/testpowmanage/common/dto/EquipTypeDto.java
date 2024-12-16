package com.example.testpowmanage.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipTypeDto {
    private Long typeId;
    private String typeName;
    private Map<String,String> data;//名称，数据类型
}
