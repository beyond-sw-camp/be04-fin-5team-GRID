package org.highfives.grid.department.service;

import org.highfives.grid.department.dao.DepartmentMapper;
import org.highfives.grid.department.dto.DepartmentDTO;
import org.highfives.grid.department.entity.Department;
import org.highfives.grid.department.exception.DepartmentNotFoundException;
import org.highfives.grid.department.repository.DepartmentRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService{


    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;
    private final ModelMapper mapper;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper, ModelMapper mapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.mapper = mapper;
    }

    @Override
    public DepartmentDTO findDepartmentById(int id) {
        // 소스 객체와 대상 객체 간에 필드를 매핑할 때 사용되는 매핑 전략을 정의
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Department findDepartmentInfo = null;

        try {
            findDepartmentInfo = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));

            return mapper.map(findDepartmentInfo, DepartmentDTO.class);
        } catch (DepartmentNotFoundException e) {
            throw new RuntimeException(e);
        }

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

    @Override
    public DepartmentDTO registDepartment(DepartmentDTO departmentDTO) {

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);

        Department department = Department.builder()
                .name(departmentDTO.getName())
                .departmentStatus("Y")
                .startTime(formattedDate)
                .build();

        departmentRepository.save(department);

        return mapper.map(department, DepartmentDTO.class);
    }
}
