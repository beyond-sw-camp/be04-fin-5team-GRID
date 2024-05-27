package org.highfives.grid.work.command.service;


import org.highfives.grid.work.common.dto.AdTimeDTO;

public interface AdTimeService {
    AdTimeDTO addStartTime(AdTimeDTO adTimeDTO);

    AdTimeDTO addEndTime(AdTimeDTO adTimeDTO);

    AdTimeDTO findAdTime(String dateTime, int employeeId);
}
