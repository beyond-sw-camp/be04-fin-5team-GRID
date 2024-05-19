package org.highfives.grid.work.command.service;

import org.highfives.grid.work.command.aggregate.AdTime;
import org.highfives.grid.work.command.repository.AdTimeRepository;
import org.highfives.grid.work.common.dto.AdTimeDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value="CommandAdTimeServiceImpl")
public class AdTimeServiceImpl implements AdTimeService {

    private final AdTimeRepository adTimeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AdTimeServiceImpl(AdTimeRepository adTimeRepository, ModelMapper modelMapper) {
        this.adTimeRepository = adTimeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AdTimeDTO addStartTime(AdTimeDTO adTimeDTO) {

        AdTime adTime = new AdTime(
                adTimeDTO.getStartTime(),
                adTimeDTO.getEmployeeId()
        );
        adTimeRepository.save(adTime);

        AdTime addAdTime = adTimeRepository.findByStartTimeAndEmployeeId(adTimeDTO.getStartTime(), adTimeDTO.getEmployeeId());

        return modelMapper.map(addAdTime, AdTimeDTO.class);
    }

    @Override
    @Transactional
    public AdTimeDTO addEndTime(AdTimeDTO adTimeDTO) {

        String time = adTimeDTO.getEndTime().split(" ")[0]; // 날짜 부분만 저장

        /* 설명. 직원id와 출근 날짜로 조회, 변경할 endTime 추가*/
        AdTime findAdTime = adTimeRepository.findAdTimeByEmployeeIdAndEndTime(time, adTimeDTO.getEmployeeId());

        if (findAdTime != null) {
            findAdTime.setEndTime(adTimeDTO.getEndTime());
        }

        AdTime addAdTime = adTimeRepository.findAdTimeByEmployeeIdAndEndTime(time, adTimeDTO.getEmployeeId());

        return modelMapper.map(addAdTime,  AdTimeDTO.class);
    }
}
