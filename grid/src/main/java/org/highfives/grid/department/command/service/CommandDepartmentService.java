package org.highfives.grid.department.command.service;

import org.highfives.grid.department.command.dto.DepartmentDTO;

import java.util.List;

public interface CommandDepartmentService {
    DepartmentDTO findDepartmentById(int id);

    DepartmentDTO registDepartment(DepartmentDTO departmentDTO);

    DepartmentDTO modifyDepartment(DepartmentDTO departmentDTO);

    void deleteDepartment(int id);
}
