package org.highfives.grid.department.query.service;

import org.highfives.grid.department.query.dto.DepartmentDTO;

import java.util.List;

public interface QueryDepartmentService {


    List<DepartmentDTO> findAllDepartment();

}
