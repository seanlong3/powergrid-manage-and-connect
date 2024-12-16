package com.example.testpowmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("equip_cast_equip_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder//????????????????
public class EquipCastEquipType {
    @TableId(value = "equip_cast_equip_type_id",type = IdType.ASSIGN_ID)
    private Long equipCaseEquipTypeId;
    @TableField("equip_id")
    private String equipId;
    @TableField("equip_name")
    private String equipName;
    @TableField("equip_type_id")
    private Long equipTypeId;
    @TableField("model_id")
    private String modelId;
}
