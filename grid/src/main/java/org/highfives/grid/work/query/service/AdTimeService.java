package org.highfives.grid.work.query.service;

import org.highfives.grid.work.common.dto.AdTimeDTO;

import java.util.List;

public interface AdTimeService {

    List<AdTimeDTO> findAdTimeByEmployeeId(int employeeId);

    List<AdTimeDTO> findAdTimeAll();

    List<AdTimeDTO> findAdTimeByStartTime(String startTime);
}
