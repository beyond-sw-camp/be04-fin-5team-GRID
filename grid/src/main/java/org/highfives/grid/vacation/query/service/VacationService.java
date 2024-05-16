package org.highfives.grid.vacation.query.service;

import org.highfives.grid.vacation.query.dto.VacationHistoryDTO;
import org.highfives.grid.vacation.query.dto.VacationInfoDTO;
import org.highfives.grid.vacation.query.dto.VacationPolicyDTO;
import org.highfives.grid.vacation.query.entity.VacationPolicy;

import java.util.List;

public interface VacationService {
    List<VacationInfoDTO> getAllVacations();
    List<VacationInfoDTO> getUserVacations(int employeeId);

    List<VacationPolicyDTO> getVacationPolicy(int typeId);

    List<VacationHistoryDTO> getAllVacationHistory();

    List<VacationInfoDTO> searchVacationInfoByName(String name);

    List<VacationInfoDTO> searchVacationInfoByDept(String dept);

    List<VacationHistoryDTO> searchVacationHistory(int typeId, int changeTypeId, int employeeId);
}
