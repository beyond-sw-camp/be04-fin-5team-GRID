package org.highfives.grid.vacation.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.vacation.query.entity.VacationPolicy;
import org.highfives.grid.vacation.query.dto.VacationTypeDTO;
import org.highfives.grid.vacation.query.entity.VacationType;
import org.highfives.grid.vacation.query.dto.VacationHistoryDTO;
import org.highfives.grid.vacation.query.entity.VacationHistory;
import org.highfives.grid.vacation.query.entity.VacationInfo;
import org.highfives.grid.vacation.query.vo.VacationPolicyWithTypeName;


import java.util.List;

@Mapper
public interface VacationMapper {

    List<VacationInfo> selectAllVacationInfo();

    List<VacationInfo> selectUserVacationInfo(int employeeId);

    VacationPolicyWithTypeName selectAllPolicy(int id);

    List<VacationHistoryDTO> vacationHistoryTable();

    List<VacationInfo> searchVacationInfoByName(String name);

    List<VacationInfo> searchVacationInfoByDept(String dept);

    List<VacationHistory> searchVacationHistories(int typeId, int changeTypeId, int employeeId);

    List<VacationPolicyWithTypeName> selectRealAllPolicy();

    List<VacationTypeDTO> selectAllType();

    List<VacationTypeDTO> selectAllTypeNoUseYn();

    VacationTypeDTO selectTypeById(int id);

    List<VacationHistory> searchVacationHistoryById(String employeeNumber);

    List<VacationHistory> searchVacationHistoryByName(String employeeName);

    List<VacationHistoryDTO> userVacationHistoryTable(int employeeId);
}