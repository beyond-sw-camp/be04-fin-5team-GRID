package org.highfives.grid.performance_review.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.performance_review.query.dto.PerformanceReviewDTO;

import java.util.List;

@Mapper
public interface PerformanceReviewMapper {
    List<PerformanceReviewDTO> selectReviewByWriterId(int employeeId);
}
