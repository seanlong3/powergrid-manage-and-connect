package com.example.testpowmanage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("all_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    @TableId(value = "permission_id")
    private Long permissionId;
    @TableField("permission_name")
    private String permissionName;
    @TableField("url")
    private String url;
}
