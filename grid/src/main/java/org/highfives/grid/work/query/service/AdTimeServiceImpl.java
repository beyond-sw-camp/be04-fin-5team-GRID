package org.highfives.grid.work.query.service;

import org.highfives.grid.work.query.aggregate.QueryAdTime;
import org.highfives.grid.work.query.dto.QueryAdTimeDTO;
import org.highfives.grid.work.query.repository.AdTimeMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Component("QueryAdTimeServiceImpl")
public class AdTimeServiceImpl implements AdTimeService {

    private final AdTimeMapper adTimeMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public AdTimeServiceImpl(AdTimeMapper adTimeMapper, ModelMapper modelMapper) {
        this.adTimeMapper = adTimeMapper;
        this.modelMapper = modelMapper;
    }


    // 조회한 값이 없을 때 예외 처리 추가
    @Override
    public QueryAdTimeDTO findAdTimeByEmployeeIdAndStartTime(QueryAdTimeDTO queryAdTimeDTO) {
        String time = queryAdTimeDTO.getEndTime().split(" ")[0]; // 날짜 부분만 저장

        QueryAdTime queryAdTime = new QueryAdTime(time, queryAdTimeDTO.getEmployeeId());
        QueryAdTime findQueryAdTime = adTimeMapper.selectAdTimeByEmployeeIdAndEndTime(queryAdTime);


        return modelMapper.map(findQueryAdTime, QueryAdTimeDTO.class);
    }

    @Override
    public List<QueryAdTimeDTO> findAdTimeByEmployeeId(int employeeId) {

        List<QueryAdTime> findQueryAdTimeList = adTimeMapper.selectAdTimeByEmployee(employeeId);

        List<QueryAdTimeDTO> findQueryAdTimeDTOList = findQueryAdTimeList.stream()
                .map(data -> modelMapper.map(data, QueryAdTimeDTO.class))
                .collect(Collectors.toList());

        return findQueryAdTimeDTOList;
    }

    @Override
    public List<QueryAdTimeDTO> findAdTimeAll() {

        List<QueryAdTime> findQueryAdTimeList = adTimeMapper.selectAdTimeAll();

        System.out.println(findQueryAdTimeList);

        List<QueryAdTimeDTO> findQueryAdTimeDTOList = findQueryAdTimeList.stream()
                .map(data -> modelMapper.map(data, QueryAdTimeDTO.class))
                .collect(Collectors.toList());

        return findQueryAdTimeDTOList;
    }
}
