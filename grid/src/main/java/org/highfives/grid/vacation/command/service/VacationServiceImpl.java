package org.highfives.grid.vacation.command.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.highfives.grid.user.command.aggregate.Gender;
import org.highfives.grid.user.query.dto.UserDTO;
import org.highfives.grid.user.query.service.UserService;
import org.highfives.grid.vacation.command.entity.VacationHistory;
import org.highfives.grid.vacation.command.entity.VacationInfo;
import org.highfives.grid.vacation.command.entity.VacationPolicy;
import org.highfives.grid.vacation.command.entity.VacationType;
import org.highfives.grid.vacation.command.repository.VacationHistoryRepository;
import org.highfives.grid.vacation.command.repository.VacationInfoRepository;
import org.highfives.grid.vacation.command.repository.VacationPolicyRepository;
import org.highfives.grid.vacation.command.repository.VacationTypeRepository;
import org.highfives.grid.vacation.command.vo.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service(value = "VacationCommandService")
@Slf4j
public class VacationServiceImpl implements VacationService {

    private VacationPolicyRepository vacationPolicyRepository;
    private VacationInfoRepository vacationInfoRepository;
    private VacationHistoryRepository vacationHistoryRepository;
    private VacationTypeRepository vacationTypeRepository;
    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public VacationServiceImpl(VacationPolicyRepository vacationPolicyRepository, VacationInfoRepository vacationInfoRepository, VacationHistoryRepository vacationHistoryRepository, VacationTypeRepository vacationTypeRepository, UserService userService, ModelMapper modelMapper) {
        this.vacationPolicyRepository = vacationPolicyRepository;
        this.vacationInfoRepository = vacationInfoRepository;
        this.vacationHistoryRepository = vacationHistoryRepository;
        this.vacationTypeRepository = vacationTypeRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void modifyVacationPolicy(ModifyPolicy policyInfo, int id) {
        VacationPolicy foundPolicy = vacationPolicyRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        foundPolicy.setContent(policyInfo.getContent());
    }

    @Override
    @Transactional
    public void deleteVacationPolicy(int id) {
        vacationPolicyRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void registVacationPolicy(RegistPolicy policyInfo) {
        VacationPolicy inputVacationPolicy = VacationPolicy.builder()
                .content(policyInfo.getContent())
                .typeId(policyInfo.getTypeId())
                .build();
        vacationPolicyRepository.save(inputVacationPolicy);
    }

    // 입사한지 1년이 안된 직원들에게 달에 한개씩 월차를 제공하는 메서드
    @Override
    @Transactional
    public void giveAnnualVacationBeforeYear() {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        String firstDayString = firstDayOfMonth.toString();
        LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        String lastDayString = lastDayOfMonth.toString();
        List<UserDTO> employees = userService.findList();

        // 1년은 안되고, 1달은 지난 직원이 사용안한 월차가 있으면 삭제하고 그 기록을 vacation_history에 저장
        for (int i = 1; i < employees.size(); i++) {
            int userId = employees.get(i).getId();
            int day = countDays(userId);
            int months = countMonths(userId);

            if (day < 365 && months >= 1) {
                if (vacationInfoRepository.findByEmployeeIdAndTypeId(userId, 2) != null) {
                    if (vacationInfoRepository.findByEmployeeIdAndTypeId(userId, 2).getVacationNum() != 0) {
                        VacationHistory inputVacationHistory = VacationHistory.builder()
                                .changeTime(firstDayString)
                                .changeReason("사용기한 만료로 인한 월차 소멸")
                                .typeId(2)
                                .changeTypeId(2)
                                .employeeId(userId)
                                .changeNum(vacationInfoRepository.findByEmployeeIdAndTypeId(userId, 2).getVacationNum())
                                .build();
                        vacationInfoRepository.deleteByTypeIdAndEmployeeId(2, userId);
                        vacationHistoryRepository.save(inputVacationHistory);
                    } else {
                        vacationInfoRepository.deleteByTypeIdAndEmployeeId(2, userId);

                    }
                }
            }
        }

        // 1년은 안되고, 1달은 지난 직원에게 월차를 제공하고 그 기록을 vacation_history에 저장
        for (int i = 1; i < employees.size(); i++) {
            int userId = employees.get(i).getId();
            int day = countDays(userId);
            int months = countMonths(userId);

            if (day < 365 && months >= 1) {
                VacationInfo inputVacationInfo = VacationInfo.builder()
                        .vacationNum(1)
                        .addTime(firstDayString)
                        .endTime(lastDayString)
                        .employeeId(userId)
                        .typeId(2)
                        .build();

                vacationInfoRepository.save(inputVacationInfo);

                VacationHistory inputVacationHistory = VacationHistory.builder()
                        .changeTime(firstDayString)
                        .changeReason("월 갱신에 따른 월차 자동 부여")
                        .typeId(2)
                        .changeTypeId(1)
                        .employeeId(userId)
                        .changeNum(1)
                        .build();

                vacationHistoryRepository.save(inputVacationHistory);
            }
        }
    }

    // 입사한지 1년이 지난 직원들에게 연차를 제공하는 메서드
    @Override
    @Transactional
    public void giveAnnualVacationAfterYear() {
        LocalDate firstDayOfYear = LocalDate.now().withDayOfYear(1);
        String firstDayString = firstDayOfYear.toString();
        LocalDate lastDayOfYear = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());
        String lastDayString = lastDayOfYear.toString();
        List<UserDTO> employees = userService.findList();

        // 입사 후 1년이 지난 직원들이 사용안한 연차가 있다면 삭제하고 그 기록을 vacation_history에 저장
        for (int i = 1; i < employees.size(); i++) {
            int userId = employees.get(i).getId();
            int day = countDays(userId);

            if (day >= 365) {
                if (vacationInfoRepository.findByEmployeeIdAndTypeId(userId, 1) != null) {
                    if (vacationInfoRepository.findByEmployeeIdAndTypeId(userId, 1).getVacationNum() != 0) {
                        VacationHistory inputVacationHistory = VacationHistory.builder()
                                .changeTime(firstDayString)
                                .changeReason("사용기한 만료로 인한 연차 소멸")
                                .typeId(1)
                                .changeTypeId(2)
                                .employeeId(userId)
                                .changeNum(vacationInfoRepository.findByEmployeeIdAndTypeId(userId, 1).getVacationNum())
                                .build();
                        vacationInfoRepository.deleteByTypeIdAndEmployeeId(1, userId);
                        vacationHistoryRepository.save(inputVacationHistory);
                    } else {
                        vacationInfoRepository.deleteByTypeIdAndEmployeeId(1, userId);
                    }
                }
            }
        }

        // 입사 후 1년이 지난 직원들에게 연차를 제공하고 그 기록을 vacation_history에 저장
        for (int i = 1; i < employees.size(); i++) {
            int userId = employees.get(i).getId();
            int day = countDays(userId);
            int months = countMonths(userId);
            double vacationNum = countVacation(day);

            if (day >= 365) {
                VacationInfo inputVacationInfo = VacationInfo.builder()
                        .vacationNum(vacationNum)
                        .addTime(firstDayString)
                        .endTime(lastDayString)
                        .employeeId(userId)
                        .typeId(1)
                        .build();

                vacationInfoRepository.save(inputVacationInfo);

                VacationHistory inputVacationHistory = VacationHistory.builder()
                        .changeTime(firstDayString)
                        .changeReason("연도 갱신에 따른 연차 자동 부여")
                        .typeId(1)
                        .changeTypeId(1)
                        .employeeId(userId)
                        .changeNum(vacationNum)
                        .build();

                vacationHistoryRepository.save(inputVacationHistory);
                // 1년이 안된 직원들은 작년에 얼마나 다녔는지에 따라 당해 연차를 제공
            } else if (day < 365 && months >= 1) {
                VacationInfo inputVacationInfo = VacationInfo.builder()
                        .vacationNum(months)
                        .addTime(firstDayString)
                        .endTime(lastDayString)
                        .employeeId(userId)
                        .typeId(1)
                        .build();

                vacationInfoRepository.save(inputVacationInfo);

                VacationHistory inputVacationHistory = VacationHistory.builder()
                        .changeTime(firstDayString)
                        .changeReason("연도 갱신에 따른 연차 자동 부여")
                        .typeId(1)
                        .changeTypeId(1)
                        .employeeId(userId)
                        .changeNum(months)
                        .build();

                vacationHistoryRepository.save(inputVacationHistory);
            }

        }
    }

    // 각각의 직원에게 정기휴가를 insert하는 메서드
    @Override
    @Transactional
//    @Scheduled(cron = "0 0 0 1 1 ?")
    public void giveRegularVacation() {
        LocalDate firstDayOfYear = LocalDate.now().withDayOfYear(1);
        String firstDayString = firstDayOfYear.toString();
        LocalDate lastDayOfYear = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());
        String lastDayString = lastDayOfYear.toString();
        List<UserDTO> employees = userService.findList();

        // 휴가를 새로 insert 하기 전, 기존의 휴가가 남아있다면 삭제하고, 그 기록을 vacation_history에 저장
        for (int i = 1; i < employees.size(); i++) {
            int userId = employees.get(i).getId();

            try {
                if (vacationInfoRepository.findByEmployeeIdAndTypeId(userId, 4) != null) {
                    if (vacationInfoRepository.findByEmployeeIdAndTypeId(userId, 4).getVacationNum() != 0) {
                        VacationHistory inputVacationHistory = VacationHistory.builder()
                                .changeTime(firstDayString)
                                .changeReason("사용기한 만료로 인한 정기휴가 소멸")
                                .typeId(4)
                                .changeTypeId(2)
                                .employeeId(userId)
                                .changeNum(vacationInfoRepository.findByEmployeeIdAndTypeId(userId, 4).getVacationNum())
                                .build();
                        vacationInfoRepository.deleteByTypeIdAndEmployeeId(4, userId);
                        vacationHistoryRepository.save(inputVacationHistory);
                    } else {
                        vacationInfoRepository.deleteByTypeIdAndEmployeeId(4, userId);

                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

        // 휴가를 새로 insert 하고, 그 기록을 vacation_history에 저장
        for (int i = 1; i < employees.size(); i++) {
            int userId = employees.get(i).getId();

            VacationInfo inputVacationInfo = VacationInfo.builder()
                    .vacationNum(4)
                    .addTime(firstDayString)
                    .endTime(lastDayString)
                    .employeeId(userId)
                    .typeId(4)
                    .build();

            vacationInfoRepository.save(inputVacationInfo);

            VacationHistory inputVacationHistory = VacationHistory.builder()
                    .changeTime(firstDayString)
                    .changeReason("연도 갱신에 따른 정기휴가 자동 부여")
                    .typeId(4)
                    .changeTypeId(1)
                    .employeeId(userId)
                    .changeNum(4)
                    .build();

            vacationHistoryRepository.save(inputVacationHistory);
        }
    }

    // 여자직원에게 보건휴가를 insert하는 메서드
    @Override
    @Transactional
//    @Scheduled(cron = "0 0 0 1 1 ?")
    public void giveHealthVacation() {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        String firstDayString = firstDayOfMonth.toString();
        LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        String lastDayString = lastDayOfMonth.toString();
        List<UserDTO> employees = userService.findList();

        // 기존의 보건휴가가 남아있으면 지우고, 그 이력을 vacation_history에 저장
        for (int i = 1; i < employees.size(); i++) {
            if (employees.get(i).getGender() == Gender.F) {
                int userId = employees.get(i).getId();
                System.out.println(userId);
                try {
                    if (vacationInfoRepository.findByEmployeeIdAndTypeId(userId, 3) != null) {
                        if (vacationInfoRepository.findByEmployeeIdAndTypeId(userId, 3).getVacationNum() != 0) {
                            VacationHistory inputVacationHistory = VacationHistory.builder()
                                    .changeTime(firstDayString)
                                    .changeReason("사용기한 만료로 인한 보건휴가 소멸")
                                    .typeId(3)
                                    .changeTypeId(2)
                                    .employeeId(userId)
                                    .changeNum(vacationInfoRepository.findByEmployeeIdAndTypeId(userId, 3).getVacationNum())
                                    .build();
                            vacationInfoRepository.deleteByTypeIdAndEmployeeId(3, userId);
                            vacationHistoryRepository.save(inputVacationHistory);
                        } else {
                            vacationInfoRepository.deleteByTypeIdAndEmployeeId(3, userId);
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }

        // 보건휴가를 새로 insert 하고, 그 이력을 vacation_history에 저장
        for (int i = 1; i < employees.size(); i++) {
            if (employees.get(i).getGender() == Gender.F) {
                int userId = employees.get(i).getId();

                VacationInfo inputVacationInfo = VacationInfo.builder()
                        .vacationNum(1)
                        .addTime(firstDayString)
                        .endTime(lastDayString)
                        .employeeId(userId)
                        .typeId(3)
                        .build();

                vacationInfoRepository.save(inputVacationInfo);

                VacationHistory inputVacationHistory = VacationHistory.builder()
                        .changeTime(firstDayString)
                        .changeReason("월 갱신에 따른 보건휴가 자동 부여")
                        .typeId(3)
                        .changeTypeId(1)
                        .employeeId(userId)
                        .changeNum(1)
                        .build();

                vacationHistoryRepository.save(inputVacationHistory);
            }
        }
    }

    @Override
    @Transactional
    public void giveVacationByManager(GiveVacation vacationInfo) {
        LocalDate today = LocalDate.now();
        String day = today.toString();
        String employeeNum = vacationInfo.getEmployeeNum();
        int employeeId = userService.findUserByEmployeeNum(employeeNum, null).getId();

        VacationInfo inputVacationInfo = VacationInfo.builder()
                .addTime(day)
                .vacationNum(vacationInfo.getVacationNum())
                .endTime(vacationInfo.getEndTime())
                .employeeId(employeeId)
                .typeId(vacationInfo.getTypeId())
                .build();

        vacationInfoRepository.save(inputVacationInfo);

        VacationHistory inputVacationHistory = VacationHistory.builder()
                .changeTime(day)
                .changeReason("관리자에 의한 직접 지급")
                .typeId(vacationInfo.getTypeId())
                .changeTypeId(1)
                .employeeId(employeeId)
                .changeNum(vacationInfo.getVacationNum())
                .build();


        vacationHistoryRepository.save(inputVacationHistory);
    }

    @Override
    @Transactional
    public void registVacationType(RegistVacationType typeInfo) {
        if(typeInfo.getTimeCheck().equals("Y")) {
            VacationType vacationType = VacationType.builder()
                    .typeName(typeInfo.getTypeName())
                    .timeCheck(typeInfo.getTimeCheck())
                    .vacationTime(typeInfo.getVacationTime())
                    .vacationNum(typeInfo.getVacationNum())
                    .dateOfUse(typeInfo.getDateOfUse())
                    .vacationExplain(typeInfo.getVacationExplain())
                    .useYn("Y")
                    .build();
            vacationTypeRepository.save(vacationType);
        } else {
            VacationType vacationType = VacationType.builder()
                    .typeName(typeInfo.getTypeName())
                    .timeCheck(typeInfo.getTimeCheck())
                    .vacationTime(0)
                    .vacationNum(typeInfo.getVacationNum())
                    .dateOfUse(typeInfo.getDateOfUse())
                    .vacationExplain(typeInfo.getVacationExplain())
                    .useYn("Y")
                    .build();
            vacationTypeRepository.save(vacationType);

        }

    }

    @Override
    @Transactional
    public void modifyVacationType(ModifyVacationType typeInfo, int id) {
        VacationType vacationType = vacationTypeRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        vacationType.setVacationNum(typeInfo.getVacationNum());
        vacationType.setVacationTime(typeInfo.getVacationTime());
        vacationType.setTimeCheck(typeInfo.getTimeCheck());
        vacationType.setDateOfUse(typeInfo.getDateOfUse());
        vacationType.setVacationExplain(typeInfo.getVacationExplain());
        vacationType.setUseYn(typeInfo.getUseYn());
    }

    @Override
    @Transactional
    public void deleteVacationType(int id) {
        vacationTypeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void plusVacationNum(int employeeId, int typeId, long days) {
        VacationInfo vacationInfo = new VacationInfo();
        if (typeId == 5 || typeId == 6) {
            vacationInfo = vacationInfoRepository.findByEmployeeIdAndTypeId(employeeId, 1);
        } else {
            vacationInfo = vacationInfoRepository.findByEmployeeIdAndTypeId(employeeId, typeId);
        }
        LocalDate today = LocalDate.now();
        String vacationTypeName = vacationTypeRepository.findById(typeId).get().getTypeName();

        if (typeId == 5) {
            vacationInfo.setVacationNum(vacationInfo.getVacationNum() + 0.5);
            VacationHistory inputVacationHistory = VacationHistory.builder()
                    .changeTime(today.toString())
                    .changeReason(vacationTypeName + " 사용취소로 인한 " + vacationTypeName + " 개수 증가")
                    .typeId(typeId)
                    .changeTypeId(4)
                    .employeeId(employeeId)
                    .changeNum(0.5)
                    .build();
        } else if (typeId == 6) {
            vacationInfo.setVacationNum(vacationInfo.getVacationNum() + 0.25);
            VacationHistory inputVacationHistory = VacationHistory.builder()
                    .changeTime(today.toString())
                    .changeReason(vacationTypeName + " 사용취소로 인한 " + vacationTypeName + " 개수 증가")
                    .typeId(typeId)
                    .changeTypeId(4)
                    .employeeId(employeeId)
                    .changeNum(0.25)
                    .build();
        } else
            vacationInfo.setVacationNum(vacationInfo.getVacationNum() + days);
            VacationHistory inputVacationHistory = VacationHistory.builder()
                    .changeTime(today.toString())
                    .changeReason(vacationTypeName + " 사용취소로 인한 " + vacationTypeName + " 개수 증가")
                    .typeId(typeId)
                    .changeTypeId(4)
                    .employeeId(employeeId)
                    .changeNum(days)
                    .build();



        vacationHistoryRepository.save(inputVacationHistory);

    }

    @Override
    @Transactional
    public void minusVacationNum(int employeeId, int typeId, long days) {
        VacationInfo vacationInfo = new VacationInfo();
        if (typeId == 5 || typeId == 6) {
            vacationInfo = vacationInfoRepository.findByEmployeeIdAndTypeId(employeeId, 1);
        } else {
            vacationInfo = vacationInfoRepository.findByEmployeeIdAndTypeId(employeeId, typeId);
        }
        LocalDate today = LocalDate.now();
        String vacationTypeName = vacationTypeRepository.findById(typeId).get().getTypeName();

        if (typeId == 5) {
            vacationInfo.setVacationNum(vacationInfo.getVacationNum() - 0.5);
            VacationHistory inputVacationHistory = VacationHistory.builder()
                    .changeTime(today.toString())
                    .changeReason(vacationTypeName + " 사용으로 인한 " + vacationTypeName + " 개수 감소")
                    .typeId(typeId)
                    .changeTypeId(3)
                    .employeeId(employeeId)
                    .changeNum(0.5)
                    .build();
        } else if (typeId == 6) {
            vacationInfo.setVacationNum(vacationInfo.getVacationNum() - 0.25);
            VacationHistory inputVacationHistory = VacationHistory.builder()
                    .changeTime(today.toString())
                    .changeReason(vacationTypeName + " 사용으로 인한 " + vacationTypeName + " 개수 감소")
                    .typeId(typeId)
                    .changeTypeId(3)
                    .employeeId(employeeId)
                    .changeNum(0.25)
                    .build();
        } else
            vacationInfo.setVacationNum(vacationInfo.getVacationNum() - days);
            VacationHistory inputVacationHistory = VacationHistory.builder()
                    .changeTime(today.toString())
                    .changeReason(vacationTypeName + " 사용으로 인한 " + vacationTypeName + " 개수 감소")
                    .typeId(typeId)
                    .changeTypeId(3)
                    .employeeId(employeeId)
                    .changeNum(days)
                    .build();

        vacationHistoryRepository.save(inputVacationHistory);
    }

    // 입사이후 총 몇일이 지났는지 계산하는 메서드
    private int countDays(int userId) {
        UserDTO employees = userService.findUserById(userId);
        LocalDateTime today = LocalDateTime.now();

        String day = employees.getJoinTime();
        LocalDate localDate = LocalDate.parse(day);
        LocalDateTime joinDay = localDate.atStartOfDay();
        Duration duration = Duration.between(joinDay, today);
        long days = duration.toDays();
        int annuals = ((int) days);


        return annuals;
    }

    // 입사이후 총 몇달이 지났는지 계산하는 메서드
    private int countMonths(int userId) {
        UserDTO employees = userService.findUserById(userId);
        LocalDate today = LocalDate.now();

        String day = employees.getJoinTime();
        LocalDate joinDate = LocalDate.parse(day);

        Period period = Period.between(joinDate, today);
        int months = period.getMonths(); // 차이를 월 단위로 얻기

        return months;

    }

    // 몇년차 인지에 따라 연차 개수를 부여하는 메서드
    private double countVacation(int days) {
        int year = days / 365;
        double vacation = 0;
        if (year >= 1 && year < 3) {
            vacation = 15;
        } else if (year < 5) {
            vacation = 16;
        } else if (year < 7) {
            vacation = 17;
        } else if (year < 9) {
            vacation = 18;
        } else if (year < 11) {
            vacation = 19;
        } else
            vacation = 20;

        return vacation;
    }
}