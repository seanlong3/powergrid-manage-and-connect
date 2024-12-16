package com.example.testpowmanage.common.dto;

import lombok.Data;

import java.math.BigDecimal;

//DTO是前端即http请求传给后端并且转化而成的对象，用于后端处理数据
//VO是后端传给前端的自定义数据格式，一般为包含各种所需数据的对象
@Data
public class AreaDto {
    private String areaName;
    private BigDecimal areaLongitude;
    private BigDecimal areaLatitude;
    private String parentName;
}
