package com.example.testpowmanage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@TableName("acquisition")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcquisitionData {
    @TableId("acquisition_id")
    private String acquisitionId;
    @TableField("equip_id")
    private String equipId;
    @TableField(exist = false)
    private Map<String,Object> dataMap;
}
