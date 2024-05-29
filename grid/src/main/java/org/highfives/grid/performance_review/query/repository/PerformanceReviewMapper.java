package org.highfives.grid.performance_review.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.performance_review.query.dto.DetailPerformanceReviewDTO;
import org.highfives.grid.performance_review.query.dto.PerformanceReviewDTO;
import org.highfives.grid.performance_review.query.dto.ReviewItemDTO;

import java.util.List;

@Mapper
public interface PerformanceReviewMapper {
    List<PerformanceReviewDTO> selectReviewByWriterId(int employeeId);

    List<PerformanceReviewDTO> selectReviewByArroverId(int employeeId);

    DetailPerformanceReviewDTO selectDetailReviewById(int id);

    List<ReviewItemDTO> selectReviewItemByReviewId(int id);
}
