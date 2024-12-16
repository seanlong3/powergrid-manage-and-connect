package com.example.testpowmanage.service;

import com.example.testpowmanage.common.dto.AreaDto;
import com.example.testpowmanage.entity.PowerGridArea;

import java.util.List;

public interface AreaService {

    List<PowerGridArea> selectAllArea();

    Boolean addArea(AreaDto areaDto);

    Boolean deleteAreaById(Long areaId);
}
