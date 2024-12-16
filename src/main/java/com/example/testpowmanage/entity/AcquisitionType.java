package com.example.testpowmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("acquisition_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcquisitionType {
    @TableId(value = "acquisition_id",type = IdType.ASSIGN_ID)
    private Long acquisitionId;
    @TableField("acquisition_name")
    private String acquisitionName;
    @TableField("stable_name")
    @JsonIgnore
    private String stableName;
}
