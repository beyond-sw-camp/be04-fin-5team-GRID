package org.highfives.grid.vacation.command.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.highfives.grid.vacation.command.entity.VacationPolicy;
import org.highfives.grid.vacation.command.repository.VacationPolicyRepository;
import org.highfives.grid.vacation.command.vo.ModifyPolicy;
import org.highfives.grid.vacation.command.vo.RegistPolicy;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "VacationCommandService")
@Slf4j
public class VacationServiceImpl implements VacationService{

    private VacationPolicyRepository vacationPolicyRepository;
    private ModelMapper modelMapper;

    @Autowired
    public VacationServiceImpl(VacationPolicyRepository vacationPolicyRepository, ModelMapper modelMapper) {
        this.vacationPolicyRepository = vacationPolicyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void modifyVacationPolicy(ModifyPolicy policyInfo, int id) {
        VacationPolicy foundPolicy = vacationPolicyRepository.findById(Long.valueOf(id)).orElseThrow(IllegalAccessError::new);
        foundPolicy.setContent(policyInfo.getContent());
    }

    @Override
    @Transactional
    public void deleteVacationPolicy(int id) {
        vacationPolicyRepository.deleteById(Long.valueOf(id));
    }

    @Override
    @Transactional
    public void registVacationPolicy(RegistPolicy policyInfo) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        VacationPolicy vacationPolicy = modelMapper.map(policyInfo, VacationPolicy.class);
        vacationPolicyRepository.save(vacationPolicy);
    }



}
