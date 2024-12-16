package com.example.testpowmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("equip_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipType {
    @TableId(value = "type_id",type = IdType.ASSIGN_ID)
    private Long typeId;
    @TableField("type_name")
    private String typeName;
    @TableField("equip_tb_name")
    @JsonIgnore
    private String equipTbName;
}
