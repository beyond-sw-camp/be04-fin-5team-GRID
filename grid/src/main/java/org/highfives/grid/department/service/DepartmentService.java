package org.highfives.grid.department.service;

import org.highfives.grid.department.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {
    DepartmentDTO findDepartmentById(int id);

    List<DepartmentDTO> findAllDepartment();
}
