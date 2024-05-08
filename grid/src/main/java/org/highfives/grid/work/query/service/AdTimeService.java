package org.highfives.grid.work.query.service;

import org.highfives.grid.work.query.dto.QueryAdTimeDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("QueryAdTimeService")
public interface AdTimeService {

    QueryAdTimeDTO findAdTimeByEmployeeIdAndStartTime(QueryAdTimeDTO queryAdTimeDTO);

    List<QueryAdTimeDTO> findAdTimeByEmployeeId(int employeeId);

    List<QueryAdTimeDTO> findAdTimeAll();
}
