package org.highfives.grid.work.query.service;

import org.highfives.grid.work.common.dto.AdTimeDTO;
import org.highfives.grid.work.query.repository.AdTimeMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value="QueryAdTimeServiceImpl")
public class AdTimeServiceImpl implements AdTimeService {

    private final AdTimeMapper adTimeMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public AdTimeServiceImpl(AdTimeMapper adTimeMapper, ModelMapper modelMapper) {
        this.adTimeMapper = adTimeMapper;
        this.modelMapper = modelMapper;
    }

    // 조회한 값이 없을 때 예외 추가 필요
    @Override
    public List<AdTimeDTO> findAdTimeByEmployeeId(int employeeId) {

        return adTimeMapper.selectAdTimeByEmployee(employeeId);
    }

    @Override
    public List<AdTimeDTO> findAdTimeAll() {

        System.out.println(adTimeMapper.selectAdTimeAll());
        return adTimeMapper.selectAdTimeAll();
    }

    // 날짜로 조회
    @Override
    public List<AdTimeDTO> findAdTimeByStartTime(String startTime) {

        System.out.println(startTime);
        System.out.println(adTimeMapper.selectAdTimeByStartTime(startTime));
        return adTimeMapper.selectAdTimeByStartTime(startTime);
    }
}
