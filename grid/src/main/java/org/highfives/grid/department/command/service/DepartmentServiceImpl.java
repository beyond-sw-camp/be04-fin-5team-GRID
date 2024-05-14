package org.highfives.grid.department.command.service;

import org.highfives.grid.department.command.dto.DepartmentDTO;
import org.highfives.grid.department.command.aggregate.Department;
import org.highfives.grid.department.command.exception.DepartmentNotFoundException;
import org.highfives.grid.department.command.repository.DepartmentRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service(value = "commandDepartmentService")
public class DepartmentServiceImpl implements DepartmentService {


    private final DepartmentRepository departmentRepository;
    private final ModelMapper mapper;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, ModelMapper mapper) {
        this.departmentRepository = departmentRepository;
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

    @Override
    public DepartmentDTO modifyDepartment(DepartmentDTO departmentDTO) {
        DepartmentDTO currentDepartmentInfo = findDepartmentById(departmentDTO.getId());

        if(!departmentDTO.getDepartmentStatus().equals("N")) {
            Department department = Department.builder()
                    .id(departmentDTO.getId())
                    .name(departmentDTO.getName())
                    .departmentStatus(departmentDTO.getDepartmentStatus())
                    .startTime(currentDepartmentInfo.getStartTime())
                    .endTime(currentDepartmentInfo.getEndTime())
                    .highDepartment(departmentDTO.getHighDepartment())
                    .memberCnt(currentDepartmentInfo.getMemberCnt())
                    .leaderId(departmentDTO.getLeaderId())
                    .build();

            departmentRepository.save(department);
            return mapper.map(department, DepartmentDTO.class);
        }

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(currentDate);

            Department department = Department.builder()
                    .id(departmentDTO.getId())
                    .name(departmentDTO.getName())
                    .departmentStatus(departmentDTO.getDepartmentStatus())
                    .startTime(currentDepartmentInfo.getStartTime())
                    .endTime(formattedDate)
                    .highDepartment(departmentDTO.getHighDepartment())
                    .memberCnt(currentDepartmentInfo.getMemberCnt())
                    .leaderId(departmentDTO.getLeaderId())
                    .build();

            departmentRepository.save(department);
            return mapper.map(department, DepartmentDTO.class);

    }

    @Override
    public void deleteDepartment(int id) {

        departmentRepository.deleteById(id);

    }
}
