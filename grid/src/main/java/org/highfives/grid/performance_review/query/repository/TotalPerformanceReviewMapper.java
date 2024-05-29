package org.highfives.grid.performance_review.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.performance_review.query.dto.TotalPerformanceReviewDTO;

import java.util.List;

@Mapper
public interface TotalPerformanceReviewMapper {
    List<TotalPerformanceReviewDTO> selectTotalByRevieweeId(int employeeId);

    List<TotalPerformanceReviewDTO> selectTotalByReviewerId(int employeeId);

    List<TotalPerformanceReviewDTO> selectAllTotal();

    TotalPerformanceReviewDTO selectTotalById(int id);
}
