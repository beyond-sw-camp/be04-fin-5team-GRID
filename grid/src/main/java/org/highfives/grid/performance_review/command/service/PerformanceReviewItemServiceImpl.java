package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.aggregate.entity.PerformanceReviewGoalItem;
import org.highfives.grid.performance_review.command.aggregate.entity.PerformanceReviewItem;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalItemDTO;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewItemDTO;
import org.highfives.grid.performance_review.command.repository.PerformanceReviewItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "commandPerformanceReviewItemServiceImpl")
public class PerformanceReviewItemServiceImpl implements PerformanceReviewItemService{

    private final PerformanceReviewItemRepository performanceReviewItemRepository;
    private final ModelMapper modelMapper;

    private final PerformanceReviewGoalItemService performanceReviewGoalItemService;


    @Autowired
    public PerformanceReviewItemServiceImpl(PerformanceReviewItemRepository performanceReviewItemRepository,
                                            ModelMapper modelMapper,
                                            PerformanceReviewGoalItemService performanceReviewGoalItemService) {
        this.performanceReviewItemRepository = performanceReviewItemRepository;
        this.modelMapper = modelMapper;
        this.performanceReviewGoalItemService = performanceReviewGoalItemService;
    }

    @Override
    @Transactional
    public List<PerformanceReviewItemDTO> addNewItems(int goalId, int reviewId) {
        List<PerformanceReviewGoalItemDTO> performanceReviewGoalItemList = performanceReviewGoalItemService.findByGoalId(goalId);
        System.out.println(performanceReviewGoalItemList);

        List<PerformanceReviewItemDTO> addReviewList = new ArrayList<>();
        for (PerformanceReviewGoalItemDTO performanceReviewGoalItem : performanceReviewGoalItemList) {
            PerformanceReviewItem performanceReviewItem = new PerformanceReviewItem(
                    performanceReviewGoalItem.getJobName(),
                    performanceReviewGoalItem.getGoal(),
                    performanceReviewGoalItem.getMetric(),
                    performanceReviewGoalItem.getPlan(),
                    performanceReviewGoalItem.getWeight(),
                    3,
                    75*performanceReviewGoalItem.getWeight()*0.01,
                    3,
                    75*performanceReviewGoalItem.getWeight()*0.01,
                    reviewId
            );

            PerformanceReviewItem addItem = performanceReviewItemRepository.save(performanceReviewItem);
            addReviewList
                    .add(modelMapper
                            .map(addItem, PerformanceReviewItemDTO.class));
        }

        return addReviewList;
    }

    @Override
    public PerformanceReviewItemDTO modifyItem(PerformanceReviewItemDTO performanceReviewItemDTO) {
        System.out.println(performanceReviewItemDTO);

        PerformanceReviewItem performanceReviewItem = performanceReviewItemRepository.findById(performanceReviewItemDTO.getId())
                .orElseThrow(IllegalArgumentException::new);

        if (performanceReviewItem != null) {
//            performanceReviewItem.setActionItem(performanceReviewItemDTO.getActionItem());
//            performanceReviewItem.setDetailPlan(performanceReviewItemDTO.getDetailPlan());
            performanceReviewItem.setPerformance(performanceReviewItemDTO.getPerformance());
            performanceReviewItem.setSelfId(performanceReviewItemDTO.getSelfId());
            performanceReviewItem.setSelfScore(performanceReviewItemDTO.getSelfScore());
            performanceReviewItem.setSelfComment(performanceReviewItemDTO.getSelfComment());
            performanceReviewItem.setSuperiorId(performanceReviewItemDTO.getSuperiorId());
            performanceReviewItem.setSuperiorScore(performanceReviewItemDTO.getSuperiorScore());
        }

        PerformanceReviewItem modifyItem = performanceReviewItemRepository.findById(
                performanceReviewItemDTO.getId()
        ).orElseThrow(IllegalArgumentException::new);

        System.out.println(modifyItem);
        return modelMapper.map(modifyItem, PerformanceReviewItemDTO.class);
    }

    @Override
    public List<PerformanceReviewItemDTO> findByReviewId(int id) {
        List<PerformanceReviewItem> performanceReviewItemList = performanceReviewItemRepository.findByReviewId(id);

        List<PerformanceReviewItemDTO> performanceReviewItemDTOList = performanceReviewItemList.stream()
                .map(performanceReviewItem -> modelMapper.map(performanceReviewItem, PerformanceReviewItemDTO.class))
                .collect(Collectors.toList());

        return performanceReviewItemDTOList;
    }

}
