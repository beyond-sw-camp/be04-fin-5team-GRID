package org.highfives.grid.department.dao;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.department.entity.Department;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    List<Department> findAllDepartment();
}
