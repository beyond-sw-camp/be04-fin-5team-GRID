package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.aggregate.entity.PerformanceReviewGoalItem;
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

    // id가 없는 항목은 새로 추가
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

        PerformanceReviewGoalItem saveGoalItem = performanceReviewGoalItemRepository.save(performanceReviewGoalItem);

//        PerformanceReviewGoalItem saveGoalItem = performanceReviewGoalItemRepository.findByGoal(
//                performanceReviewGoalItem.getGoal()
//        );

        System.out.println(saveGoalItem);

        return modelMapper.map(saveGoalItem, PerformanceReviewGoalItemDTO.class);
    }

    // id가 있는 항목은 수정
    @Override
    @Transactional
    public PerformanceReviewGoalItemDTO modifyGoalItem(PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO) {
        System.out.println(performanceReviewGoalItemDTO);

        PerformanceReviewGoalItem goalItem = performanceReviewGoalItemRepository
                                                        .findById(performanceReviewGoalItemDTO.getId())
                .orElseThrow(IllegalArgumentException::new);

        System.out.println(goalItem);

        if(goalItem != null) {
            goalItem.setJobName(performanceReviewGoalItemDTO.getJobName());
            goalItem.setGoal(performanceReviewGoalItemDTO.getGoal());
            goalItem.setMetric(performanceReviewGoalItemDTO.getMetric());
            goalItem.setWeight(performanceReviewGoalItemDTO.getWeight());
            goalItem.setPlan(performanceReviewGoalItemDTO.getPlan());
        }

        PerformanceReviewGoalItem modifyGoalItem = performanceReviewGoalItemRepository.findById(
                performanceReviewGoalItemDTO.getId())
                .orElseThrow(IllegalArgumentException::new);

        System.out.println(modifyGoalItem);

        return modelMapper.map(modifyGoalItem, PerformanceReviewGoalItemDTO.class);
    }

    // 목표 항목 삭제
    @Override
    @Transactional
    public int removeGoalItem(int id){
        PerformanceReviewGoalItem findItem = performanceReviewGoalItemRepository.findById(id).orElseThrow();
        performanceReviewGoalItemRepository.deleteById(id);

        return id;
    }

}
