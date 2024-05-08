package org.highfives.grid.work.query.service;

import org.highfives.grid.work.query.dto.AdTimeDTO;

import java.util.List;

public interface AdTimeService {

    AdTimeDTO findAdTimeByEmployeeIdAndStartTime(AdTimeDTO adTimeDTO);

    List<AdTimeDTO> findAdTimeByEmployeeId(int employeeId);
}
