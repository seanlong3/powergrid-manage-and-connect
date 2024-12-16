package com.example.testpowmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@TableName("repair_routine_inspection")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairRoutineInspection {
    @TableId(value = "repair_routine_inspection_id",type = IdType.ASSIGN_ID)
    private Long repairRoutineInspectionId;
    @TableField("repair_id")
    private String repairId;
    @TableField("area_id")
    private Long areaId;
    @TableField("route_record")
    private String routeRecord;
}
