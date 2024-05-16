package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.aggregate.GoalApprovalStatus;
import org.highfives.grid.performance_review.command.aggregate.entity.PerformanceReviewGoal;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalDTO;
import org.highfives.grid.performance_review.command.repository.PerformanceReviewGoalRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service(value = "commandGoalServiceImpl")
public class PerformanceReviewGoalServiceImpl implements PerformanceReviewGoalService{

    private final PerformanceReviewGoalRepository performanceReviewGoalRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PerformanceReviewGoalServiceImpl(
            PerformanceReviewGoalRepository performanceReviewGoalRepository, ModelMapper modelMapper) {
        this.performanceReviewGoalRepository = performanceReviewGoalRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public PerformanceReviewGoalDTO addNewGoal(PerformanceReviewGoalDTO performanceReviewGoalDTO) {
        System.out.println(performanceReviewGoalDTO);

        // 작성중 기본값으로 변경
        PerformanceReviewGoal goal = new PerformanceReviewGoal(
                performanceReviewGoalDTO.getId(),
                performanceReviewGoalDTO.getYear(),
                performanceReviewGoalDTO.getReviewName(),
                performanceReviewGoalDTO.getApprovalStatus(),
                performanceReviewGoalDTO.getWriterId(),
                performanceReviewGoalDTO.getWriteTime(),
                performanceReviewGoalDTO.getApproverId(),
                performanceReviewGoalDTO.getApprovalTime()
        );

        System.out.println(goal);
        performanceReviewGoalRepository.save(goal);

        System.out.println("저장완료");
        PerformanceReviewGoal saveGoal = performanceReviewGoalRepository.findByYearAndWriterId(
                performanceReviewGoalDTO.getYear(),
                performanceReviewGoalDTO.getWriterId()
        );
        System.out.println(saveGoal);
        PerformanceReviewGoalDTO saveGoalDTO = modelMapper.map(saveGoal, PerformanceReviewGoalDTO.class);
        System.out.println(saveGoalDTO);
        return saveGoalDTO;
    }

    // 작성 중인 상태로 변경
    @Override
    public PerformanceReviewGoalDTO modifyGoalStatusInProgress(int id) {
        PerformanceReviewGoal performanceReviewGoal = performanceReviewGoalRepository.findById(id).orElseThrow();

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(currentTime);

        performanceReviewGoal.setApprovalStatus(String.valueOf(GoalApprovalStatus.IP));
        performanceReviewGoal.setWriteTime(now);

        PerformanceReviewGoal modifyGoal = performanceReviewGoalRepository.findById(id).orElseThrow();
        PerformanceReviewGoalDTO modifyGoalDTO = modelMapper.map(modifyGoal, PerformanceReviewGoalDTO.class);

        return modifyGoalDTO;
    }
}

