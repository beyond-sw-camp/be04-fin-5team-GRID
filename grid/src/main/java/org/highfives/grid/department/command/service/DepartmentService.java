package org.highfives.grid.department.command.service;

import org.highfives.grid.department.command.dto.DepartmentDTO;

public interface DepartmentService {
    DepartmentDTO findDepartmentById(int id);

    DepartmentDTO registDepartment(DepartmentDTO departmentDTO);

    DepartmentDTO modifyDepartment(DepartmentDTO departmentDTO);

    void deleteDepartment(int id);
}
