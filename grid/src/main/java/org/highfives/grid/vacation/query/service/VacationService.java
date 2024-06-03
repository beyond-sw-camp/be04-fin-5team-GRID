package org.highfives.grid.vacation.query.service;


import org.highfives.grid.vacation.query.vo.*;


public interface VacationService {
    ResVacationInfoVO getAllVacations();

    ResVacationInfoVO getUserVacations(int employeeId);

    ResVacationPolicyVO getVacationPolicy(int id);

    ResVacationHistoryVO getAllVacationHistory();

    ResVacationInfoVO searchVacationInfoByName(String name);

    ResVacationInfoVO searchVacationInfoByDept(String dept);

    ResVacationHistoryVO searchVacationHistory(int typeId, int changeTypeId, int employeeId);

    ResVacationPolicyWithTypeNameVO getAllVacationPolicy();

    ResVacationTypeVO getVacationType();

    ResOneVacationTypeVO getVacationTypeById(int id);

    ResVacationHistoryVO searchVacationHistoryByName(String employeeName);

    ResVacationHistoryVO searchVacationHistoryById(String employeeNumber);

    ResVacationHistoryVO getUserVacationHistory(int employeeId);

    ResVacationTypeVO getAllVacationType();
}