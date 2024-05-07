package org.highfives.grid.department.service;

import org.highfives.grid.department.dto.DepartmentDTO;

public interface DepartmentService {
    DepartmentDTO findDepartmentById(int id);
}
