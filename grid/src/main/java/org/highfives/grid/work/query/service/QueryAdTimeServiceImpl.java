package org.highfives.grid.work.query.service;

import org.highfives.grid.work.query.aggregate.QueryAdTime;
import org.highfives.grid.work.query.dto.QueryAdTimeDTO;
import org.highfives.grid.work.query.repository.AdTimeMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueryAdTimeServiceImpl implements QueryAdTimeService{

    private final AdTimeMapper adTimeMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public QueryAdTimeServiceImpl(AdTimeMapper adTimeMapper, ModelMapper modelMapper) {
        this.adTimeMapper = adTimeMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public QueryAdTimeDTO findAdTimeByEmployeeIdAndStartTime(QueryAdTimeDTO queryAdTimeDTO) {
        String time = queryAdTimeDTO.getEndTime().split(" ")[0]; // 날짜 부분만 저장

        QueryAdTime queryAdTime = new QueryAdTime(time, queryAdTimeDTO.getEmployeeId());
        QueryAdTime findAdTime = adTimeMapper.selectAdTimeByEmployeeIdAndEndTime(queryAdTime);


        return modelMapper.map(findAdTime, QueryAdTimeDTO.class);
    }

}
