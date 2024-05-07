package org.highfives.grid.work.query.service;

import org.highfives.grid.work.query.dto.QueryAdTimeDTO;

public interface QueryAdTimeService {

    QueryAdTimeDTO findAdTimeByEmployeeIdAndStartTime(QueryAdTimeDTO queryAdTimeDTO);
}
