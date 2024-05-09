package org.highfives.grid.vacation.query.service;

import lombok.extern.slf4j.Slf4j;
import org.highfives.grid.vacation.query.dto.VacationHistoryDTO;
import org.highfives.grid.vacation.query.dto.VacationInfoDTO;
import org.highfives.grid.vacation.query.dto.VacationPolicyDTO;
import org.highfives.grid.vacation.query.entity.VacationHistory;
import org.highfives.grid.vacation.query.entity.VacationInfo;
import org.highfives.grid.vacation.query.entity.VacationPolicy;
import org.highfives.grid.vacation.query.repository.VacationMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service(value = "VacationQueryService")
@Slf4j
public class VacationServiceImpl implements VacationService{
    private VacationMapper vacationMapper;
    private ModelMapper modelMapper;

    @Autowired
    public VacationServiceImpl(VacationMapper vacationMapper, ModelMapper modelMapper) {
        this.vacationMapper = vacationMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<VacationInfoDTO> getAllVacations() {
        List<VacationInfo> vacations = vacationMapper.selectAllVacationInfo();

        return vacations.stream().map(vacation -> modelMapper.map(vacation, VacationInfoDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<VacationInfoDTO> getUserVacations(int employeeId) {
        List<VacationInfo> vacations = vacationMapper.selectUserVacationInfo(employeeId);

        return vacations.stream().map(vacation -> modelMapper.map(vacation, VacationInfoDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<VacationPolicyDTO> getVacationPolicy(int typeId) {
        List<VacationPolicy> policies = vacationMapper.selectAllPolicy(typeId);

        return policies.stream().map(policy -> modelMapper.map(policy, VacationPolicyDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<VacationHistoryDTO> getAllVacationHistory() {
        List<VacationHistory> histories = vacationMapper.selectAllHistories();

        return histories.stream().map(history -> modelMapper.map(history, VacationHistoryDTO.class)).collect(Collectors.toList());
    }

}
