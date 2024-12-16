package com.example.testpowmanage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

//这个表如何创建出来的？
@Data
public class ColumnInfo {
    @TableField("column_name")
    private String columnName;
    @TableField("data_type")
    private String dataType;
}
