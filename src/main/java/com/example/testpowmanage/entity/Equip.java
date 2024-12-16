package com.example.testpowmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@TableName("equip")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Equip {
    @TableId(value = "equip_id",type = IdType.ASSIGN_ID)
    private String equipId;
    @TableField("type_id")
    private Long typeId;
    @TableField("equip_name")
    private String equipName;
    @TableField("model_id")
    private String modelId;
    @TableField("area_id")
    private Long areaId;
    @TableField(exist = false)//table中不存储的数据
    private Map<String, Object> data = new HashMap<>();
}
