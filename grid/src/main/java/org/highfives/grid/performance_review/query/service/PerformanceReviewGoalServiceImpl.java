package org.highfives.grid.performance_review.query.service;

import org.highfives.grid.performance_review.query.aggregate.PerformanceReviewGoal;
import org.highfives.grid.performance_review.query.dto.PerformanceReviewGoalDTO;
import org.highfives.grid.performance_review.query.repository.PerformanceReviewGoalMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceReviewGoalServiceImpl implements PerformanceReviewGoalService{

    private final PerformanceReviewGoalMapper performanceReviewGoalMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public PerformanceReviewGoalServiceImpl(PerformanceReviewGoalMapper performanceReviewGoalMapper, ModelMapper modelMapper) {
        this.performanceReviewGoalMapper = performanceReviewGoalMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PerformanceReviewGoalDTO> findAllGoalByWriterId(int employeeId) {
        System.out.println(employeeId);
        List<PerformanceReviewGoalDTO> findGoalList = performanceReviewGoalMapper.selectAllGoalByWriterId(employeeId);
        System.out.println(findGoalList);
        return findGoalList;
    }
}
