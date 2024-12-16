package com.example.testpowmanage.service.impl;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.testpowmanage.common.vo.*;
import com.example.testpowmanage.entity.*;
import com.example.testpowmanage.mapper.*;
import com.example.testpowmanage.service.AcquisitionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class AcquisitionServiceImpl implements AcquisitionService {
    private final AcquisitionTypeMapper acquisitionTypeMapper;
    private final AcquisitionDataMapper acquisitionDataMapper;
    private final AcquisitionToEquipTypeMapper acquisitionToEquipTypeMapper;
    private final EquipTypeMapper equipTypeMapper;
    private final AcquisitionEquipCastMapper acquisitionEquipCastMapper;

    private final EquipCastEquipTypeMapper equipCastEquipTypeMapper;

    @Autowired
    public AcquisitionServiceImpl(AcquisitionTypeMapper acquisitionTypeMapper, AcquisitionDataMapper acquisitionDataMapper, AcquisitionToEquipTypeMapper acquisitionToEquipTypeMapper, EquipTypeMapper equipTypeMapper, AcquisitionEquipCastMapper acquisitionEquipCastMapper, EquipCastEquipTypeMapper equipCastEquipTypeMapper) {
        this.acquisitionTypeMapper = acquisitionTypeMapper;
        this.acquisitionDataMapper = acquisitionDataMapper;
        this.acquisitionToEquipTypeMapper = acquisitionToEquipTypeMapper;
        this.equipTypeMapper = equipTypeMapper;
        this.acquisitionEquipCastMapper = acquisitionEquipCastMapper;
        this.equipCastEquipTypeMapper = equipCastEquipTypeMapper;
    }

    @Override
    public List<AcquisitionTypeVO> listAllAcquisitonType() {
        List<AcquisitionType> acquisitionTypeList = acquisitionTypeMapper.selectList(null);
        List<AcquisitionTypeVO> resultList = new ArrayList<>();
        for (AcquisitionType type : acquisitionTypeList) {
            Map<String, String> acquisitionParams = new HashMap<>();
            //填充获取参数表
            getAcquisitionDetail(type, acquisitionParams);
            resultList.add(AcquisitionTypeVO.builder()
                    .acquisitionId(type.getAcquisitionId())
                    .acquisitionName(type.getAcquisitionName())
                    .acquisitionParams(acquisitionParams)
                    .build());
        }
        return resultList;
    }

    private void getAcquisitionDetail(AcquisitionType type, Map<String, String> acquisitionParams) {
        List<ColumnInfo> columnInfos;
        DynamicDataSourceContextHolder.push("tdengine");
        try {
            columnInfos = acquisitionDataMapper.selectStableDataColumnByStableName(type.getStableName());
        } finally {
            DynamicDataSourceContextHolder.clear();
        }
        for (ColumnInfo columnInfo : columnInfos) {
            if (!"_ts".equals(columnInfo.getColumnName())) {
                acquisitionParams.put(columnInfo.getColumnName(), columnInfo.getDataType());
            }
        }
    }


    @Override
    public EquipTypeAcquisitionVO getEquipTypeHasAcquisitionByEquipTypeId(Long equipTypeId) {
        List<AcquisitionType> acquisitionTypeList = getAcquisitionTypeByEquipTypeId(equipTypeId);
        if (acquisitionTypeList == null) {
            return null;
        }
        List<EquipTypeAcquisitionVO.AcquisitionMetadata> metadataList = new ArrayList<>();
        EquipType equipType = equipTypeMapper.selectById(equipTypeId);
        if (equipType == null) {
            return null;
        }
        //获取采集点数据的操作
        for (AcquisitionType acquisitionType : acquisitionTypeList) {
            Map<String, String> paramMap = new HashMap<>();
            getAcquisitionDetail(acquisitionType, paramMap);
            metadataList.add(new EquipTypeAcquisitionVO.AcquisitionMetadata(
                            acquisitionType.getAcquisitionId(),
                            acquisitionType.getAcquisitionName(),
                            paramMap
                    )
            );
        }
        return new EquipTypeAcquisitionVO(equipType.getTypeName(), metadataList);
    }

    //2.有了采集id之后，就能拿到采集类型的list了
    public List<AcquisitionType> getAcquisitionTypeByEquipTypeId(Long equipTypeId) {
        List<Long> acquisitionIds = getAcquisitionIdByEquipTypeId(equipTypeId);
        if (acquisitionIds == null) {
            return List.of();
        }
        return acquisitionTypeMapper.selectBatchIds(acquisitionIds);
    }

    //1.通过设备类型id，拿到采集id
    private List<Long> getAcquisitionIdByEquipTypeId(Long equipTypeId) {
        return acquisitionToEquipTypeMapper.selectList(new QueryWrapper<AcquisitionToEquipType>()
                        .eq("type_id", equipTypeId)).stream()
                .map(AcquisitionToEquipType::getAcquisitionId).toList();
    }

    @Override
    public EquipAcquisitionDataVO getEquipAcquisitionData(String equipId, String startTime, String endTime) {
        List<AcquisitionEquipCast> acquisitionEquipCasts =
                acquisitionEquipCastMapper.selectByMap(Map.of("equip_id", equipId));//可以换吧
        List<EquipAcquisitionDataVO.AcquisitionMetaData> result = new ArrayList<>();
        for (AcquisitionEquipCast item : acquisitionEquipCasts) {
            log.info(item);
            DynamicDataSourceContextHolder.push("tdengine");
            List<Map<String, Object>> dataMap;//这里的select待改正
            try {
                dataMap = acquisitionDataMapper.selectAcquisitionDataByChildtable(
                        item.getChildTableName(),
                        startTime,
                        endTime
                );
            } finally {
                DynamicDataSourceContextHolder.clear();
            }
            String acquisitionTypeName = acquisitionTypeMapper.selectByMap(Map.of(
                    "stable_name", item.getStableName()
            )).get(0).getAcquisitionName();
            result.add(new EquipAcquisitionDataVO.AcquisitionMetaData(
                    item.getAcquisitionEquipId(),
                    acquisitionTypeName,
                    dataMap
            ));
        }
        return new EquipAcquisitionDataVO(equipId, result);
    }

    @Override
    public List<Map<String, Object>> getAcquisitionDataByAcquisitionEquipId(String acquisitionEquipId, String startTime, String endTime) {
        AcquisitionEquipCast acquisitionEquipCast =
                acquisitionEquipCastMapper.selectById(acquisitionEquipId);
        if (acquisitionEquipCast == null) {
            return null;
        }
//        List<EquipAcquisitionDataVO.AcquisitionMetaData> result = new ArrayList<>();   没用到！！！！
        DynamicDataSourceContextHolder.push("tdengine");
        List<Map<String, Object>> dataMap;
        try {
            dataMap = acquisitionDataMapper.selectAcquisitionDataByChildtable(
                    acquisitionEquipCast.getChildTableName(),
                    startTime,
                    endTime
            );
        } finally {
            DynamicDataSourceContextHolder.clear();
        }
        return dataMap;
    }


    @Override
    public List<AcquisitionEquipVO> getEquipAcquisitionEquipsByAcquisitionTypeId(Long acquisitionTypeId) {
        AcquisitionType acquisitionType = acquisitionTypeMapper.selectById(acquisitionTypeId);
        List<AcquisitionEquipCast> acquisitionEquipCastList = acquisitionEquipCastMapper.selectList(
                new QueryWrapper<AcquisitionEquipCast>().eq("stable_name", acquisitionType.getStableName())
        );
        List<AcquisitionEquipVO> resultList = new ArrayList<>();
        for (AcquisitionEquipCast item : acquisitionEquipCastList) {
            EquipCastEquipType equipCastEquipType = equipCastEquipTypeMapper.selectOne(
                    new QueryWrapper<EquipCastEquipType>().eq("equip_id", item.getEquipId())
            );
            resultList.add(AcquisitionEquipVO.builder()
                    .acquisitionEquipId(item.getAcquisitionEquipId())
                    .equipId(item.getEquipId())
                    .equipName(equipCastEquipType.getEquipName())
                    .modelId(equipCastEquipType.getModelId())
                    .build());
        }
        return resultList;
    }

    @Override
    public List<AcquisitionTypeCastEquipTypeVO> getAllAcquisitionTypeCastData(Long equipTypeId) {
        List<AcquisitionType> acquisitionTypeList = acquisitionTypeMapper.selectList(null);
        Map<Long, AcquisitionTypeCastEquipTypeVO> castMap = new HashMap<>(acquisitionTypeList.size());
        for (AcquisitionType type : acquisitionTypeList) {
            AcquisitionTypeCastEquipTypeVO tempItem = AcquisitionTypeCastEquipTypeVO.builder()
                    .acquisitionTypeId(type.getAcquisitionId())
                    .acquisitionTypeName(type.getAcquisitionName())
                    .isUsed(false).build();
            castMap.put(type.getAcquisitionId(), tempItem);
        }

        List<AcquisitionType> usingAcquisitionTypeList = getAcquisitionTypeByEquipTypeId(equipTypeId);
        for (AcquisitionType usingTypeItem : usingAcquisitionTypeList) {
            castMap.get(usingTypeItem.getAcquisitionId()).setIsUsed(true);
        }

        List<AcquisitionTypeCastEquipTypeVO> resultList = new ArrayList<>(acquisitionTypeList.size());
        for (Map.Entry<Long, AcquisitionTypeCastEquipTypeVO> entry : castMap.entrySet()) {
            resultList.add(entry.getValue());
        }
        return resultList;
    }


    @Override
    public Boolean createAcquisitionType(String acquisitionTypeName, Map<String, String> paramMap) {
        if (acquisitionTypeMapper.exists(new QueryWrapper<AcquisitionType>()
                .eq("acquisition_name", acquisitionTypeName))) {
            return false;
        }
        String stableName = "t_" + UUID.randomUUID().toString().replace('-', '_');
        while (acquisitionTypeMapper.exists(new QueryWrapper<AcquisitionType>()
                .eq("stable_name", stableName))) {
            stableName = "t_" + UUID.randomUUID().toString().replace('-', '_');
        }
        acquisitionTypeMapper.insert(new AcquisitionType(
                        null,
                        acquisitionTypeName,
                        stableName
                )
        );
        DynamicDataSourceContextHolder.push("tdengine");
        try {
            acquisitionDataMapper.createAcquisitionStable(paramMap, stableName);
            acquisitionDataMapper.createAcquisitionEquipChildtable(
                    "default",
                    "default",
                    stableName,
                    stableName + "_default"
            );
        } finally {
            DynamicDataSourceContextHolder.clear();
        }
        return true;
    }

    @Override
    public EquipTypeSingleVO createAcquisitionEquip(String equipId, Map<String, Long> acquisitionMap) {
        //Map:采集设备id——采集种类id
        if (acquisitionMap == null || acquisitionMap.isEmpty()) {
            return null;
        }
        for (Map.Entry<String, Long> entry : acquisitionMap.entrySet()) {
            if (acquisitionEquipCastMapper.exists(new QueryWrapper<AcquisitionEquipCast>().allEq(
                    Map.of(
                            "equip_id", equipId,
                            "acquisition_equip_id", entry.getKey()
                    )
            ))) {
                return null;
            }
            AcquisitionType acquisitionType = acquisitionTypeMapper.selectById(entry.getValue());
            if (acquisitionType == null) {
                return null;
            }
            String acquisitionRulerMatchString = entry.getKey()
                    .replace('-', '_')
                    .replace(' ', '_');
            String equipIdRulerMatchString = equipId
                    .replace('-', '_')
                    .replace(' ', '_');
            String childTableName = "t_" + acquisitionRulerMatchString + equipIdRulerMatchString;
            DynamicDataSourceContextHolder.push("tdengine");
            try {
                acquisitionDataMapper.createAcquisitionEquipChildtable(
                        equipId,
                        entry.getKey(),
                        acquisitionType.getStableName(),
                        childTableName
                );
            } finally {
                DynamicDataSourceContextHolder.clear();
            }
            acquisitionEquipCastMapper.insert(new AcquisitionEquipCast(
                    entry.getKey(),
                    equipId,
                    acquisitionType.getStableName(),
                    childTableName
            ));
        }
        EquipCastEquipType equipCastEquipType = equipCastEquipTypeMapper.selectOne(new QueryWrapper<EquipCastEquipType>()
                .eq("equip_id", equipId));
        if (equipCastEquipType == null) {
            return null;
        }
        return EquipTypeSingleVO.builder().equipTypeId(equipCastEquipType.getEquipTypeId()).build();
    }

    @Override
    @Transactional
    public Boolean changeAcquisitionTypeToEquipType(Long equipTypeId, Map<Long, Boolean> acquisitionTypeId) {
        List<Long> existedAcquisitionTypeIds = getAcquisitionIdByEquipTypeId(equipTypeId);
        for (Map.Entry<Long, Boolean> entry : acquisitionTypeId.entrySet()) {
            if (entry.getValue()) {
                if (existedAcquisitionTypeIds.contains(entry.getKey())) {
                    continue;
                }
                acquisitionToEquipTypeMapper.insert(new AcquisitionToEquipType(
                        null,
                        equipTypeId,
                        entry.getKey()
                ));
            } else {
                if (!existedAcquisitionTypeIds.contains(entry.getKey())) {
                    continue;
                }
                acquisitionToEquipTypeMapper.delete(
                        new QueryWrapper<AcquisitionToEquipType>().allEq(
                                Map.of(
                                        "type_id", equipTypeId,
                                        "acquisition_id", entry.getKey()
                                )
                        )
                );
            }
        }
        return true;
    }

    @Override
    public Boolean deleteAcquisitionEquip(String acquisitionEquipId) {
        AcquisitionEquipCast acquisitionEquipCast =
                acquisitionEquipCastMapper.selectById(acquisitionEquipId);
        if (acquisitionEquipCast == null) {
            return false;
        }
        DynamicDataSourceContextHolder.push("tdengine");
        try {
            acquisitionDataMapper.deleteAcquisitionEquipChildtable(acquisitionEquipCast.getChildTableName());
        } finally {
            DynamicDataSourceContextHolder.clear();
        }
        acquisitionEquipCastMapper.deleteById(acquisitionEquipId);
        return true;
    }

    @Override
    public Boolean deleteAcquisitionType(Long acquisitionTypeId) {
        AcquisitionType acquisitionType = acquisitionTypeMapper.selectById(acquisitionTypeId);
        if (acquisitionType==null){
            return false;
        }
        List<AcquisitionEquipCast> acquisitionEquipCastList =
                acquisitionEquipCastMapper.selectList(new QueryWrapper<AcquisitionEquipCast>()
                        .eq("stable_name",acquisitionType.getStableName()));
        if (acquisitionEquipCastList != null && !acquisitionEquipCastList.isEmpty()){
            DynamicDataSourceContextHolder.push("tdengine");
            try{
                acquisitionDataMapper.deleteAcquisitionStable(acquisitionType.getStableName());
            }finally {
                DynamicDataSourceContextHolder.clear();
            }
            Set<String> acquisitionEquipIdSet = acquisitionEquipCastList.stream()
                    .map(AcquisitionEquipCast::getAcquisitionEquipId).collect(Collectors.toSet());
            acquisitionEquipCastMapper.deleteByIds(acquisitionEquipIdSet);
        }
        if (acquisitionToEquipTypeMapper.exists(
                new QueryWrapper<AcquisitionToEquipType>().eq("acquisition_id",acquisitionTypeId)
        )){
            acquisitionToEquipTypeMapper.delete(
                    new QueryWrapper<AcquisitionToEquipType>().eq("acquisition_id",acquisitionTypeId)
            );
        }
        acquisitionTypeMapper.deleteById(acquisitionTypeId);
        return true;
    }
}
