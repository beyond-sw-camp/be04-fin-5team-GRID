package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.aggregate.PerformanceReviewGoal;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalDTO;
import org.highfives.grid.performance_review.command.repository.PerformanceReviewGoalRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PerformanceReviewGoalDTO addGoal(PerformanceReviewGoalDTO performanceReviewGoalDTO) {
        System.out.println(performanceReviewGoalDTO);

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
}

