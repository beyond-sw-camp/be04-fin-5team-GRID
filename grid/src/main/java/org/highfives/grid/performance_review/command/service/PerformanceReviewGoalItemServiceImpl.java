package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.aggregate.PerformanceReviewGoal;
import org.highfives.grid.performance_review.command.aggregate.PerformanceReviewGoalItem;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalDTO;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalItemDTO;
import org.highfives.grid.performance_review.command.repository.PerformanceReviewGoalItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "commandGoalItemServiceImpl")
public class PerformanceReviewGoalItemServiceImpl implements PerformanceReviewGoalItemService{

    private final PerformanceReviewGoalItemRepository performanceReviewGoalItemRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PerformanceReviewGoalItemServiceImpl(
            PerformanceReviewGoalItemRepository performanceReviewGoalItemRepository, ModelMapper modelMapper) {
        this.performanceReviewGoalItemRepository = performanceReviewGoalItemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public PerformanceReviewGoalItemDTO addGoalItem(PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO) {
        System.out.println(performanceReviewGoalItemDTO);

        PerformanceReviewGoalItem performanceReviewGoalItem = new PerformanceReviewGoalItem(
                performanceReviewGoalItemDTO.getJobName(),
                performanceReviewGoalItemDTO.getGoal(),
                performanceReviewGoalItemDTO.getMetric(),
                performanceReviewGoalItemDTO.getWeight(),
                performanceReviewGoalItemDTO.getPlan(),
                performanceReviewGoalItemDTO.getObjection(),
                performanceReviewGoalItemDTO.getGoalId()
        );

        performanceReviewGoalItemRepository.save(performanceReviewGoalItem);

        PerformanceReviewGoalItem saveGoalItem = performanceReviewGoalItemRepository.findByGoal(
                performanceReviewGoalItem.getGoal()
        );

        System.out.println(saveGoalItem);

        return modelMapper.map(saveGoalItem, PerformanceReviewGoalItemDTO.class);
    }
}
