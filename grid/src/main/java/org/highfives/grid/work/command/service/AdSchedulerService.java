package org.highfives.grid.work.command.service;

import org.highfives.grid.work.common.dto.AdTimeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdSchedulerService {

    private final AdTimeServiceImpl adTimeServiceCommand;

    private final org.highfives.grid.work.query.service.AdTimeService adTimeServiceQuery;

    @Autowired
    public AdSchedulerService(
            @Qualifier("CommandAdTimeServiceImpl") AdTimeServiceImpl adTimeServiceCommand,
            @Qualifier("QueryAdTimeServiceImpl") org.highfives.grid.work.query.service.AdTimeService adTimeServiceQuery
    ) {
        this.adTimeServiceCommand = adTimeServiceCommand;
        this.adTimeServiceQuery = adTimeServiceQuery;
    }


    // 23시 59분까지 퇴근을 찍지 않았을 때 자동 퇴근 체크
    @Scheduled(cron = "59 59 23 * * *")
    public void autoDepartureCheck() {

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String nowDate = dateFormat.format(currentTime);

        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String nowDateTime = dateTimeFormat.format(currentTime);

        List<AdTimeDTO> adTimeDTOList = adTimeServiceQuery.findAdTimeByStartTime(nowDate);

        for (AdTimeDTO adTimeDTO : adTimeDTOList) {
            if(adTimeDTO.getEndTime() == null || adTimeDTO.getEndTime().equals("")){
                adTimeDTO.setEndTime(nowDateTime);
                adTimeServiceCommand.addEndTime(adTimeDTO);
            }
        }
    }

}
