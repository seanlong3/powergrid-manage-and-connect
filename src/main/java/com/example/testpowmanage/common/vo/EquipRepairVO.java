package com.example.testpowmanage.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipRepairVO {
    private String repairId;
    @JsonFormat(pattern = "yyyy-MM--dd HH:mm:ss")
    private LocalDateTime repairTime;
    private String manager;
    // 0正在检修 1检修完毕
    private Integer status;
    private String content;
    // 检修操作类型 0检修设备 1巡检
    private Integer type;

    private String equipId;
    private String equipName;
    private Integer hasRenewalPart;
    private Integer isWorking;
    private String modelId;

    private Long areaId;
    private String routeRecord;
}
