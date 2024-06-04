package org.highfives.grid.department.command.service;

import org.highfives.grid.department.command.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {
    DepartmentDTO findDepartmentById(int id);

    DepartmentDTO registDepartment(DepartmentDTO departmentDTO);

    DepartmentDTO modifyDepartment(DepartmentDTO departmentDTO);

    void deleteDepartment(int id);

    List<DepartmentDTO> modifyAllDepartment(List<DepartmentDTO> departmentDTO);

    List<DepartmentDTO> modifyDepartmentStatus(List<DepartmentDTO> departmentDTO);

    DepartmentDTO modifyDepartmentLeader(DepartmentDTO departmentDTO);
}
