package org.highfives.grid.vacation.query.service;

import lombok.extern.slf4j.Slf4j;
import org.highfives.grid.vacation.query.entity.VacationType;
import org.highfives.grid.vacation.query.dto.VacationHistoryDTO;
import org.highfives.grid.vacation.query.dto.VacationInfoDTO;
import org.highfives.grid.vacation.query.dto.VacationPolicyDTO;
import org.highfives.grid.vacation.query.dto.VacationTypeDTO;
import org.highfives.grid.vacation.query.entity.VacationHistory;
import org.highfives.grid.vacation.query.entity.VacationInfo;
import org.highfives.grid.vacation.query.entity.VacationPolicy;
import org.highfives.grid.vacation.query.repository.VacationMapper;
import org.highfives.grid.vacation.query.vo.*;
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
    public ResVacationPolicyVO getVacationPolicy(int id) {
        VacationPolicyWithTypeName policies = vacationMapper.selectAllPolicy(id);

        ResVacationPolicyVO resVacationPolicyVO = new ResVacationPolicyVO();
        resVacationPolicyVO.setStatusCode(200);
        resVacationPolicyVO.setMessage("조회 성공");
        resVacationPolicyVO.setResult(policies);

        return resVacationPolicyVO;
    }

    @Override
    public ResVacationHistoryVO getAllVacationHistory() {
        List<VacationHistoryDTO> histories = vacationMapper.vacationHistoryTable();

        ResVacationHistoryVO resVacationHistoryVO = new ResVacationHistoryVO();
        resVacationHistoryVO.setStatusCode(200);
        resVacationHistoryVO.setMessage("조회 성공");
        resVacationHistoryVO.setResult(histories);

        return resVacationHistoryVO;
    }

    @Override
    public ResVacationHistoryVO getUserVacationHistory(int employeeId) {
        List<VacationHistoryDTO> histories = vacationMapper.userVacationHistoryTable(employeeId);

        ResVacationHistoryVO resVacationHistoryVO = new ResVacationHistoryVO();
        resVacationHistoryVO.setStatusCode(200);
        resVacationHistoryVO.setMessage("조회 성공");
        resVacationHistoryVO.setResult(histories);

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

    @Override
    public ResVacationHistoryVO searchVacationHistoryByName(String employeeName) {
        List<VacationHistory> histories = vacationMapper.searchVacationHistoryByName(employeeName);

        ResVacationHistoryVO resVacationHistoryVO = new ResVacationHistoryVO();
        resVacationHistoryVO.setStatusCode(200);
        resVacationHistoryVO.setMessage("조회 성공");
        resVacationHistoryVO.setResult(histories.stream().map(history -> modelMapper.map(history, VacationHistoryDTO.class)).collect(Collectors.toList()));

        return resVacationHistoryVO;
    }

    @Override
    public ResVacationHistoryVO searchVacationHistoryById(String employeeNumber) {
        List<VacationHistory> histories = vacationMapper.searchVacationHistoryById(employeeNumber);

        ResVacationHistoryVO resVacationHistoryVO = new ResVacationHistoryVO();
        resVacationHistoryVO.setStatusCode(200);
        resVacationHistoryVO.setMessage("조회 성공");
        resVacationHistoryVO.setResult(histories.stream().map(history -> modelMapper.map(history, VacationHistoryDTO.class)).collect(Collectors.toList()));

        return resVacationHistoryVO;
    }

    @Override
    public ResVacationPolicyWithTypeNameVO getAllVacationPolicy() {
        List<VacationPolicyWithTypeName> policies = vacationMapper.selectRealAllPolicy();

        ResVacationPolicyWithTypeNameVO resVacationPolicyVO = new ResVacationPolicyWithTypeNameVO();
        resVacationPolicyVO.setStatusCode(200);
        resVacationPolicyVO.setMessage("조회 성공");
        resVacationPolicyVO.setResult(policies);

        return resVacationPolicyVO;
    }

    @Override
    public ResVacationTypeVO getVacationType() {
        List<VacationTypeDTO> types = vacationMapper.selectAllType();

        ResVacationTypeVO resVacationTypeVO = new ResVacationTypeVO();
        resVacationTypeVO.setStatusCode(200);
        resVacationTypeVO.setMessage("조회 성공");
        resVacationTypeVO.setResult(types);

        return resVacationTypeVO;
    }

    @Override
    public ResVacationTypeVO getAllVacationType() {
        List<VacationTypeDTO> types = vacationMapper.selectAllTypeNoUseYn();

        ResVacationTypeVO resVacationTypeVO = new ResVacationTypeVO();
        resVacationTypeVO.setStatusCode(200);
        resVacationTypeVO.setMessage("조회 성공");
        resVacationTypeVO.setResult(types);

        return resVacationTypeVO;
    }

    @Override
    public ResOneVacationTypeVO getVacationTypeById(int id) {
        VacationTypeDTO type = vacationMapper.selectTypeById(id);

        ResOneVacationTypeVO resOneVacationTypeVO = new ResOneVacationTypeVO();
        resOneVacationTypeVO.setStatusCode(200);
        resOneVacationTypeVO.setMessage("조회 성공");
        resOneVacationTypeVO.setResult(type);

        return resOneVacationTypeVO;
    }

}