package org.highfives.grid.vacation.query.service;

import lombok.extern.slf4j.Slf4j;
import org.highfives.grid.vacation.query.dto.VacationHistoryDTO;
import org.highfives.grid.vacation.query.dto.VacationInfoDTO;
import org.highfives.grid.vacation.query.dto.VacationPolicyDTO;
import org.highfives.grid.vacation.query.entity.VacationHistory;
import org.highfives.grid.vacation.query.entity.VacationInfo;
import org.highfives.grid.vacation.query.entity.VacationPolicy;
import org.highfives.grid.vacation.query.repository.VacationMapper;
import org.highfives.grid.vacation.query.vo.ResVacationHistoryVO;
import org.highfives.grid.vacation.query.vo.ResVacationInfoVO;
import org.highfives.grid.vacation.query.vo.ResVacationPolicyVO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service(value = "VacationQueryService")
@Slf4j
public class VacationServiceImpl implements VacationService {
    private VacationMapper vacationMapper;
    private ModelMapper modelMapper;

    @Autowired
    public VacationServiceImpl(VacationMapper vacationMapper, ModelMapper modelMapper) {
        this.vacationMapper = vacationMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResVacationInfoVO getAllVacations() {
        List<VacationInfo> vacations = vacationMapper.selectAllVacationInfo();

        ResVacationInfoVO resVacationInfoVO = new ResVacationInfoVO();
        resVacationInfoVO.setStatusCode(200);
        resVacationInfoVO.setMessage("조회 성공");
        resVacationInfoVO.setResult(vacations.stream().map(vacation -> modelMapper.map(vacation, VacationInfoDTO.class)).collect(Collectors.toList()));

        return resVacationInfoVO;
    }

    @Override
    public ResVacationInfoVO getUserVacations(int employeeId) {
        List<VacationInfo> vacations = vacationMapper.selectUserVacationInfo(employeeId);

        ResVacationInfoVO resVacationInfoVO = new ResVacationInfoVO();
        resVacationInfoVO.setStatusCode(200);
        resVacationInfoVO.setMessage("조회 성공");
        resVacationInfoVO.setResult(vacations.stream().map(vacation -> modelMapper.map(vacation, VacationInfoDTO.class)).collect(Collectors.toList()));

        return resVacationInfoVO;
    }

    @Override
    public ResVacationPolicyVO getVacationPolicy(int typeId) {
        List<VacationPolicy> policies = vacationMapper.selectAllPolicy(typeId);

        ResVacationPolicyVO resVacationPolicyVO = new ResVacationPolicyVO();
        resVacationPolicyVO.setStatusCode(200);
        resVacationPolicyVO.setMessage("조회 성공");
        resVacationPolicyVO.setResult(policies.stream().map(policy -> modelMapper.map(policy, VacationPolicyDTO.class)).collect(Collectors.toList()));

        return resVacationPolicyVO;
    }

    @Override
    public ResVacationHistoryVO getAllVacationHistory() {
        List<VacationHistory> histories = vacationMapper.selectAllHistories();

        ResVacationHistoryVO resVacationHistoryVO = new ResVacationHistoryVO();
        resVacationHistoryVO.setStatusCode(200);
        resVacationHistoryVO.setMessage("조회 성공");
        resVacationHistoryVO.setResult(histories.stream().map(history -> modelMapper.map(history, VacationHistoryDTO.class)).collect(Collectors.toList()));

        return resVacationHistoryVO;
    }

    @Override
    public ResVacationInfoVO searchVacationInfoByName(String name) {
        List<VacationInfo> vacations = vacationMapper.searchVacationInfoByName(name);

        ResVacationInfoVO resVacationInfoVO = new ResVacationInfoVO();
        resVacationInfoVO.setStatusCode(200);
        resVacationInfoVO.setMessage("조회 성공");
        resVacationInfoVO.setResult(vacations.stream().map(vacation -> modelMapper.map(vacation, VacationInfoDTO.class)).collect(Collectors.toList()));

        return resVacationInfoVO;
    }

    @Override
    public ResVacationInfoVO searchVacationInfoByDept(String dept) {
        List<VacationInfo> vacations = vacationMapper.searchVacationInfoByDept(dept);

        ResVacationInfoVO resVacationInfoVO = new ResVacationInfoVO();
        resVacationInfoVO.setStatusCode(200);
        resVacationInfoVO.setMessage("조회 성공");
        resVacationInfoVO.setResult(vacations.stream().map(vacation -> modelMapper.map(vacation, VacationInfoDTO.class)).collect(Collectors.toList()));

        return resVacationInfoVO;
    }

    @Override
    public ResVacationHistoryVO searchVacationHistory(int typeId, int changeTypeId, int employeeId) {
        List<VacationHistory> histories = vacationMapper.searchVacationHistories(typeId, changeTypeId, employeeId);

        ResVacationHistoryVO resVacationHistoryVO = new ResVacationHistoryVO();
        resVacationHistoryVO.setStatusCode(200);
        resVacationHistoryVO.setMessage("조회 성공");
        resVacationHistoryVO.setResult(histories.stream().map(history -> modelMapper.map(history, VacationHistoryDTO.class)).collect(Collectors.toList()));

        return resVacationHistoryVO;
    }

}
