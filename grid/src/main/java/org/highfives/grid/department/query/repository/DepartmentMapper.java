package org.highfives.grid.department.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.department.query.aggregate.Department;

import java.util.List;

@Mapper
public interface DepartmentMapper {


    List<Department> findAllDepartment();
}
