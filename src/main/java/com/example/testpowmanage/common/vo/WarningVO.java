package com.example.testpowmanage.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class WarningVO {
    private Long warningId;
    private String equipId;
    private String equipName;
    private String modelId;
    private String warningTitle;
    private String warningContent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime warningTime;
    private Integer isSolved;
}
