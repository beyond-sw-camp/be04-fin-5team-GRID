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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                .departmentName(departmentDTO.getDepartmentName())
                .departmentStatus("Y")
                .departmentCode(departmentDTO.getDepartmentCode())
                .leaderId(departmentDTO.getLeaderId())
                .startTime(formattedDate)
                .sequence(departmentDTO.getSequence())
                .build();

        departmentRepository.save(department);

        return mapper.map(department, DepartmentDTO.class);
    }

    @Override
    public List<DepartmentDTO> modifyAllDepartment(List<DepartmentDTO> departmentDTOList) {

        List<DepartmentDTO> updateList = new ArrayList<>();

        for (DepartmentDTO modifyData : departmentDTOList) {

            DepartmentDTO currentDepartmentInfo = findDepartmentById(modifyData.getId());

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(currentDate);

            String departmentStatus = modifyData.getDepartmentStatus() != null ? modifyData.getDepartmentStatus() : "Y";

            if (!"N".equals(departmentStatus)) {
                Department department = Department.builder()
                        .id(modifyData.getId())
                        .departmentName(modifyData.getDepartmentName())
                        .departmentStatus(departmentStatus)
                        .startTime(currentDepartmentInfo.getStartTime())
                        .endTime(currentDepartmentInfo.getEndTime())
                        .memberCnt(currentDepartmentInfo.getMemberCnt())
                        .leaderId(modifyData.getLeaderId())
                        .departmentCode(modifyData.getDepartmentCode())
                        .sequence(modifyData.getSequence())
                        .build();

                departmentRepository.save(department);
                updateList.add(mapper.map(department, DepartmentDTO.class));
            } else {
                Department department = Department.builder()
                        .id(modifyData.getId())
                        .departmentName(modifyData.getDepartmentName())
                        .departmentStatus(departmentStatus)
                        .startTime(currentDepartmentInfo.getStartTime())
                        .endTime(formattedDate)
                        .memberCnt(currentDepartmentInfo.getMemberCnt())
                        .leaderId(modifyData.getLeaderId())
                        .departmentCode(modifyData.getDepartmentCode())
                        .sequence(modifyData.getSequence())
                        .build();

                departmentRepository.save(department);
                updateList.add(mapper.map(department, DepartmentDTO.class));
            }
        }

        return updateList;
    }


    @Override
    public DepartmentDTO modifyDepartment(DepartmentDTO departmentDTO) {
        DepartmentDTO currentDepartmentInfo = findDepartmentById(departmentDTO.getId());

        if(!departmentDTO.getDepartmentStatus().equals("N")) {
            Department department = Department.builder()
                    .id(departmentDTO.getId())
                    .departmentName(departmentDTO.getDepartmentName())
                    .departmentStatus(departmentDTO.getDepartmentStatus())
                    .startTime(currentDepartmentInfo.getStartTime())
                    .endTime(currentDepartmentInfo.getEndTime())
                    .memberCnt(currentDepartmentInfo.getMemberCnt())
                    .leaderId(departmentDTO.getLeaderId())
                    .departmentCode(departmentDTO.getDepartmentCode())
                    .sequence(departmentDTO.getSequence())
                    .build();

            departmentRepository.save(department);
            return mapper.map(department, DepartmentDTO.class);
        }

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(currentDate);

            Department department = Department.builder()
                    .id(departmentDTO.getId())
                    .departmentName(departmentDTO.getDepartmentName())
                    .departmentStatus(departmentDTO.getDepartmentStatus())
                    .startTime(currentDepartmentInfo.getStartTime())
                    .endTime(formattedDate)
                    .memberCnt(currentDepartmentInfo.getMemberCnt())
                    .leaderId(departmentDTO.getLeaderId())
                    .sequence(departmentDTO.getSequence())
                    .departmentCode(departmentDTO.getDepartmentCode())
                    .build();

            departmentRepository.save(department);
            return mapper.map(department, DepartmentDTO.class);

    }

    @Override
    public void deleteDepartment(int id) {

        departmentRepository.deleteById(id);

    }
}
