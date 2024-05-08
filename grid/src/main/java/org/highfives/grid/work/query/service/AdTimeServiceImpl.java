package org.highfives.grid.work.query.service;

import org.highfives.grid.work.query.aggregate.AdTime;
import org.highfives.grid.work.query.dto.AdTimeDTO;
import org.highfives.grid.work.query.repository.AdTimeMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdTimeServiceImpl implements AdTimeService {

    private final AdTimeMapper adTimeMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public AdTimeServiceImpl(AdTimeMapper adTimeMapper, ModelMapper modelMapper) {
        this.adTimeMapper = adTimeMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public AdTimeDTO findAdTimeByEmployeeIdAndStartTime(AdTimeDTO adTimeDTO) {
        String time = adTimeDTO.getEndTime().split(" ")[0]; // 날짜 부분만 저장

        AdTime adTime = new AdTime(time, adTimeDTO.getEmployeeId());
        AdTime findAdTime = adTimeMapper.selectAdTimeByEmployeeIdAndEndTime(adTime);


        return modelMapper.map(findAdTime, AdTimeDTO.class);
    }

    @Override
    public List<AdTimeDTO> findAdTimeByEmployeeId(int employeeId) {

        List<AdTime> findAdTimeList = adTimeMapper.selectAdTimeByEmployee(employeeId);

        List<AdTimeDTO> findAdTimeDTOList = findAdTimeList.stream()
                .map(data -> modelMapper.map(data, AdTimeDTO.class))
                .collect(Collectors.toList());

        return findAdTimeDTOList;
    }
}
