package org.highfives.grid.vacation.command.repository;

import org.highfives.grid.vacation.command.entity.VacationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VacationInfoRepository extends JpaRepository<VacationInfo, Integer> {
    VacationInfo findByEmployeeId(int employeeId);

    void deleteByTypeIdAndEmployeeId(int typeId, int employeeId);

    VacationInfo findByEmployeeIdAndTypeId(int userId, int typeId);
}
