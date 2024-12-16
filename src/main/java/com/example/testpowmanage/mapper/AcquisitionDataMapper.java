package com.example.testpowmanage.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.testpowmanage.entity.AcquisitionData;
import com.example.testpowmanage.entity.ColumnInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@DS("tdengine")
public interface AcquisitionDataMapper extends BaseMapper<AcquisitionData> {

    void deleteAcquisitionEquipChildtable(@Param("childTableName") String childTableName);

    List<ColumnInfo> selectStableDataColumnByStableName(@Param("stableName") String stableName);

    //??????????????????????????????????????????????????????????????????????????????????
    List<Map<String, Object>> selectAcquisitionDataByChildtable(
            @Param("tableName") String childTableName,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime);

    void createAcquisitionStable(@Param("params") Map<String, String> paramMap,
                                 @Param("stableName") String stableName);

    void createAcquisitionEquipChildtable(@Param("equipId") String equipId,
                                          @Param("acquisitionEquipId") String acquisitionEquipId,
                                          @Param("stableName") String stableName,
                                          @Param("childTableName") String childTableName);

    void deleteAcquisitionStable(@Param("stableName") String stableName);
}
