package com.example.testpowmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("all_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @TableId(value = "role_id",type = IdType.ASSIGN_ID)
    private Long roleId;
    @TableField("role_name")
    private String roleName;
}
