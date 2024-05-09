package org.highfives.grid.work.query.service;

import org.highfives.grid.work.query.dto.AdTimeDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("QueryAdTimeService")
public interface AdTimeService {

    AdTimeDTO findAdTimeByEmployeeIdAndStartTime(AdTimeDTO adTimeDTO);

    List<AdTimeDTO> findAdTimeByEmployeeId(int employeeId);

    List<AdTimeDTO> findAdTimeAll();
}
