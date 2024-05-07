package org.highfives.grid.work.command.service;

import org.highfives.grid.work.command.aggregate.AdTime;
import org.highfives.grid.work.command.dto.AdTimeDTO;
import org.highfives.grid.work.command.repository.AdTimeRepository;
import org.highfives.grid.work.query.aggregate.QueryAdTime;
import org.highfives.grid.work.query.repository.AdTimeMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdTimeServiceImpl implements AdTimeService {

    private final AdTimeRepository adTimeRepository;
    private final AdTimeMapper adTimeMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public AdTimeServiceImpl(AdTimeRepository adTimeRepository, AdTimeMapper adTimeMapper,ModelMapper modelMapper) {
        this.adTimeRepository = adTimeRepository;
        this.adTimeMapper = adTimeMapper;
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
    public AdTimeDTO addEndTime(AdTimeDTO adTimeDTO) {

        String time = adTimeDTO.getEndTime().split(" ")[0]; // 날짜 부분만 저장

        QueryAdTime queryAdTime = new QueryAdTime(time, adTimeDTO.getEmployeeId());

        /* 설명. 직원id와 출근 날짜로 변경할 endTime 추가할 데이터 조회 */
        // 추후 JPQL 사용
        QueryAdTime findAdTime = adTimeMapper.selectAdTimeByEmployeeIdAndEndTime(queryAdTime);

        if (findAdTime != null) {
            findAdTime.setEndTime(adTimeDTO.getEndTime());
        }

        QueryAdTime addAdTime = adTimeMapper.selectAdTimeByEmployeeIdAndEndTime(queryAdTime);

        return modelMapper.map(addAdTime,  AdTimeDTO.class);
    }
}
