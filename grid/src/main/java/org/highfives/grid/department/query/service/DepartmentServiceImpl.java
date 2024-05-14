package org.highfives.grid.department.query.service;

import org.highfives.grid.department.query.aggregate.Department;
import org.highfives.grid.department.query.repository.DepartmentMapper;
import org.highfives.grid.department.query.dto.DepartmentDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "QueryDepartmentService")
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentMapper departmentMapper;
    private final ModelMapper mapper;

    @Autowired
    public DepartmentServiceImpl(DepartmentMapper departmentMapper, ModelMapper mapper) {
        this.departmentMapper = departmentMapper;
        this.mapper = mapper;
    }



    @Override
    public List<DepartmentDTO> findAllDepartment() {

        // 부서 정보 리스트
        List<Department> departmentList = departmentMapper.findAllDepartment();

        // DTO형식으로 변환
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();

        for (Department department : departmentList) {
            departmentDTOList.add(mapper.map(department, DepartmentDTO.class));
        }

        return departmentDTOList;
    }
}
