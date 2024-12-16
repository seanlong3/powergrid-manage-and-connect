package com.example.testpowmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("acquisition_to_equip_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcquisitionToEquipType {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;
    @TableField("type_id")
    private Long typeId;
    @TableField("acquisition_id")
    private Long acquisitionId;
}
