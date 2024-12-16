package com.example.testpowmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("repair_facility_maintenance")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairFacilityMaintenance {
    @TableId(value = "repair_facility_maintenance_id",type = IdType.ASSIGN_ID)
    private Long repairFacilityMaintenanceId;
    @TableField("repair_id")
    private String repairId;
    @TableField("equip_id")
    private String equipId;
    @TableField("equip_name")
    private String equipName;
    @TableField("has_renewal_part")
    private Integer hasRenewalPart;
    @TableField("is_working")
    private Integer isWorking;
    @TableField("model_id")
    private String modelId;

}
