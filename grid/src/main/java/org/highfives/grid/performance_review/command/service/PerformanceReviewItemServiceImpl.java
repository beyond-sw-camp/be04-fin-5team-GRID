package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.repository.PerformanceReviewItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "commandPerformanceReviewItemServiceImpl")
public class PerformanceReviewItemServiceImpl implements PerformanceReviewItemService{

    private final PerformanceReviewItemRepository performanceReviewItemRepository;
    private final ModelMapper modelMapper;

//    private final


    @Autowired
    public PerformanceReviewItemServiceImpl(PerformanceReviewItemRepository performanceReviewItemRepository, ModelMapper modelMapper) {
        this.performanceReviewItemRepository = performanceReviewItemRepository;
        this.modelMapper = modelMapper;
    }

//    @Override
//    public PerformanceReviewItemDTO addReviewItem(PerformanceReviewItemDTO performanceReviewItemDTO) {
//        return null;
//    }
}
