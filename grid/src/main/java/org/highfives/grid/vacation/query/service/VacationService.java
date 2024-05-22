package org.highfives.grid.vacation.query.service;

import org.highfives.grid.vacation.query.vo.ResVacationHistoryVO;
import org.highfives.grid.vacation.query.vo.ResVacationInfoVO;
import org.highfives.grid.vacation.query.vo.ResVacationPolicyVO;


public interface VacationService {
    ResVacationInfoVO getAllVacations();

    ResVacationInfoVO getUserVacations(int employeeId);

    ResVacationPolicyVO getVacationPolicy(int typeId);

    ResVacationHistoryVO getAllVacationHistory();

    ResVacationInfoVO searchVacationInfoByName(String name);

    ResVacationInfoVO searchVacationInfoByDept(String dept);

    ResVacationHistoryVO searchVacationHistory(int typeId, int changeTypeId, int employeeId);
}
