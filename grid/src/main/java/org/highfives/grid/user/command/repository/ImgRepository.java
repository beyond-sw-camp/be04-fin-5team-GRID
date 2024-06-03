package org.highfives.grid.user.command.repository;

import org.highfives.grid.user.command.aggregate.EmployeeImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImgRepository extends JpaRepository<EmployeeImg, Integer> {

    EmployeeImg findByEmployeeIdAndTypeId(int id, int typeId);
}
