package org.highfives.grid.performance_review.query.service;

import org.highfives.grid.performance_review.query.dto.DetailPerformanceReviewDTO;
import org.highfives.grid.performance_review.query.dto.PerformanceReviewDTO;
import org.highfives.grid.performance_review.query.dto.ReviewItemDTO;
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

    // 팀원의 작성한 업적 평가 조회
    @Override
    public List<PerformanceReviewDTO> findPerformanceReviewByWriterId(int employeeId) {
        List<PerformanceReviewDTO> findReviewList = performanceReviewMapper.selectReviewByWriterId(employeeId);

        System.out.println(findReviewList);
        return findReviewList;
    }

    @Override
    public List<PerformanceReviewDTO> findPerformanceReviewByApproverId(int employeeId) {
        List<PerformanceReviewDTO> findReviewList = performanceReviewMapper.selectReviewByArroverId(employeeId);

        System.out.println(findReviewList);
        return findReviewList;
    }

    @Override
    public DetailPerformanceReviewDTO findDetailPerformanceReviewById(int id) {
        System.out.println(id);
        DetailPerformanceReviewDTO findReview = performanceReviewMapper.selectDetailReviewById(id);

        List<ReviewItemDTO> findReviewItem = performanceReviewMapper.selectReviewItemByReviewId(id);
        System.out.println(findReviewItem);
        System.out.println(findReview);

        findReview.setReviewItemList(findReviewItem);

        return findReview;
    }
}
