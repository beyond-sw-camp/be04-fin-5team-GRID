package org.highfives.grid.vacation.command.service;

import org.highfives.grid.vacation.command.vo.GiveVacation;
import org.highfives.grid.vacation.command.vo.ModifyPolicy;
import org.highfives.grid.vacation.command.vo.RegistPolicy;
import org.highfives.grid.vacation.command.vo.RegistVacationType;

public interface VacationService {
    void modifyVacationPolicy(ModifyPolicy policyInfo, int id);

    void deleteVacationPolicy(int id);

    void registVacationPolicy(RegistPolicy policyInfo);

    void giveAnnualVacationBeforeYear();

    void giveAnnualVacationAfterYear();

    void giveRegularVacation();

    void giveHealthVacation();

    void giveVacationByManager(GiveVacation vacationInfo);

    void registVacationType(RegistVacationType typeInfo);

    void minusVacationNum(int employeeId, int typeId);

    void plusVacationNum(int employeeId, int typeId);
}
