package com.example.testpowmanage.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.testpowmanage.entity.Equip;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@DS("opengauss")
public interface EquipMapper extends BaseMapper<Equip> {
    @Select("select * from equip where area_id = ${areaId}")
    List<Map<String, Object>> selectAllEquipByAreaId(@Param("areaId") Long areaId);//不太理解这个returnType
    //每个Map代表一行记录即一个equip的数据，Map的键是列名，值是对应的列值。
    // 这种结构允许灵活处理不同的字段而不需要预定义对象模型，适用于返回的结果列数和名称可能变化的情况。

    void insertEquipData(@Param("dataMap") Map<String, Object> dataMap,@Param("tableName") String tableName);
}
