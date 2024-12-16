package com.example.testpowmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@TableName("power_grid_area")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PowerGridArea {
    @TableId(value = "area_id",type = IdType.ASSIGN_ID)
    private Long areaId;
    @TableField("area_name")
    private String areaName;
    @TableField("area_longitude")
    private BigDecimal areaLongitude;
    @TableField("area_latitude")
    private BigDecimal areaLatitude;
    @TableField("parent_name")
    private String parentName;
}
