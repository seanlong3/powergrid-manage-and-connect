package com.example.testpowmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("acquisition_equip_cast")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcquisitionEquipCast {
    @TableId(value = "acquisition_equip_id",type = IdType.ASSIGN_ID)
    private String acquisitionEquipId;
    @TableField("equip_id")
    private String equipId;
    @TableField("stable_name")
    private String stableName;
    @TableField("child_table_name")
    private String childTableName;
}
