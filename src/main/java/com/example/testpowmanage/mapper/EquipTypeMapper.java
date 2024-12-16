package com.example.testpowmanage.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.testpowmanage.entity.ColumnInfo;
import com.example.testpowmanage.entity.EquipType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@DS("opengauss")
public interface EquipTypeMapper extends BaseMapper<EquipType> {
    @Select("select column_name, data_type from information_schema.columns where table_name = #{tableName} and table_schema = 'public'")
    List<ColumnInfo> getColumnInfo(@Param("tableName") String tableName);

    void createEquipTable(@Param("tableName") String equipTbName
            ,@Param("params") Map<String, String> params);//params:paramName-paramType

    void deleteEquipTable(@Param("tableName") String equipTbName);
}
