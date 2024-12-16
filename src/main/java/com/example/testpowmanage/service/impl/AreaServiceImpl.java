package com.example.testpowmanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.testpowmanage.common.dto.AreaDto;
import com.example.testpowmanage.repository.EquipRepository;
import com.example.testpowmanage.repository.EquipTypeRepository;
import com.example.testpowmanage.entity.Equip;
import com.example.testpowmanage.entity.EquipType;
import com.example.testpowmanage.entity.PowerGridArea;
import com.example.testpowmanage.mapper.PowerGridAreaMapper;
import com.example.testpowmanage.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
    //按理说这些数据库操作，应该额外扔进去一个are pository里的!!!
    private final PowerGridAreaMapper powerGridAreaMapper;
    private final EquipTypeRepository equipTypeRepository;
    private final EquipRepository equipRepository;

    @Autowired
    public AreaServiceImpl(PowerGridAreaMapper powerGridAreaMapper, EquipTypeRepository equipTypeRepository, EquipRepository equipRepository) {
        this.powerGridAreaMapper = powerGridAreaMapper;
        this.equipTypeRepository = equipTypeRepository;
        this.equipRepository = equipRepository;
    }

    @Override
    public List<PowerGridArea> selectAllArea() {
        return powerGridAreaMapper.selectList(null);
    }

    @Override
    public Boolean addArea(AreaDto areaDto) {
        //校验power_grid_area table中是否存在相同名称的area
        if (powerGridAreaMapper.exists(new QueryWrapper<PowerGridArea>()
                .eq("area_name",areaDto.getAreaName()))){
            return false;//虽然没问题，但是可以优化一下
        }
        powerGridAreaMapper.insert(new PowerGridArea(
                null,
                areaDto.getAreaName(),
                areaDto.getAreaLongitude(),
                areaDto.getAreaLatitude(),
                areaDto.getParentName()
        ));
        return true;//可以优化
    }


    @Override
    public Boolean deleteAreaById(Long areaId) {
        //校验power_grid_area table种是否存在
        if (!powerGridAreaMapper.exists(
             new QueryWrapper<PowerGridArea>().eq("area_id",areaId)
        )){
            return false;
        }
        //删除操作确实避免不了和其他板块的耦合！
        List<EquipType> equipTypeList = equipTypeRepository.selectAllEquipType();
        List<Equip> equipList = new ArrayList<>();
        for (EquipType equipTypeItem : equipTypeList){
            equipList.addAll(equipRepository.selectAllEquip(equipTypeItem.getEquipTbName(),areaId));
        }
        for (Equip equipItem : equipList){
            equipRepository.deleteEquip(equipItem.getEquipId(),equipItem.getTypeId());
        }
        powerGridAreaMapper.deleteById(areaId);
        return true;
    }
}
