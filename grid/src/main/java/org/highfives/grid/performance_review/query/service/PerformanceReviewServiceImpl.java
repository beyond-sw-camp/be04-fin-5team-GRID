package org.highfives.grid.performance_review.query.service;

import org.highfives.grid.performance_review.query.dto.PerformanceReviewDTO;
import org.highfives.grid.performance_review.query.repository.PerformanceReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "queryPerformanceReviewServiceImpl")
public class PerformanceReviewServiceImpl implements PerformanceReviewService{
    private final PerformanceReviewMapper performanceReviewMapper;

    @Autowired
    public PerformanceReviewServiceImpl(PerformanceReviewMapper performanceReviewMapper) {
        this.performanceReviewMapper = performanceReviewMapper;
    }


    @Override
    public List<PerformanceReviewDTO> findPerformanceReviewByWriterId(int employeeId) {
        List<PerformanceReviewDTO> findReviewList = performanceReviewMapper.selectReviewByWriterId(employeeId);

        System.out.println(findReviewList);
        return findReviewList;
    }
}
