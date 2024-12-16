package com.example.testpowmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("equip_repair")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipRepair {
    @TableId(value = "repair_id",type = IdType.ASSIGN_ID)
    private String repairId;
    @TableField("repair_time")
    private LocalDateTime repairTime;
    @TableField("manager")
    private String manager;
    @TableField("status")
    private Integer status;
    @TableField("content")
    private String content;
    // 检修操作类型 0检修设备 1巡检
    @TableField("type")
    private Integer type;
}
