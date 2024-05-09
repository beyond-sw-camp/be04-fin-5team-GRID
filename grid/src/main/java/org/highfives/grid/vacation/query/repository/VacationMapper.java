package org.highfives.grid.vacation.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.vacation.query.entity.VacationHistory;
import org.highfives.grid.vacation.query.entity.VacationInfo;
import org.highfives.grid.vacation.query.entity.VacationPolicy;

import java.util.List;

@Mapper
public interface VacationMapper {

    List<VacationInfo> selectAllVacationInfo();

    List<VacationInfo> selectUserVacationInfo(int employeeId);

    List<VacationPolicy> selectAllPolicy(int typeId);

    List<VacationHistory> selectAllHistories();
}
