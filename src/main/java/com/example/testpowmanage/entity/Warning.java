package com.example.testpowmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("warning")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warning {
    @TableId(value = "warning_id", type = IdType.ASSIGN_ID)
    private Long warningId;
    @TableField("equip_id")
    private String equipId;
    @TableField("warning_title")
    private String warningTitle;
    @TableField("warning_content")
    private String warningContent;
    @TableField("warning_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime warningTime;
    @TableField("is_solved")
    private Integer isSolved;
    @TableField("is_deleted")
    private Integer isDeleted;
}
