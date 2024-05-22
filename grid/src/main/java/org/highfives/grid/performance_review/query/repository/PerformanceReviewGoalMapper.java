package org.highfives.grid.performance_review.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.performance_review.query.aggregate.PerformanceReviewGoal;
import org.highfives.grid.performance_review.query.dto.DetailGoalDTO;
import org.highfives.grid.performance_review.query.dto.GoalItemDTO;
import org.highfives.grid.performance_review.query.dto.PerformanceReviewGoalDTO;

import java.util.List;
import java.util.Map;

@Mapper
public interface PerformanceReviewGoalMapper {
    List<PerformanceReviewGoalDTO> selectAllGoalByWriterId(int employeeId);

    List<PerformanceReviewGoalDTO> selectAllGoalByApproverId(int employeeId);

    DetailGoalDTO selectDetailGoalById(int id);

    List<GoalItemDTO> selectAllGoalItemByGoalId(int id);

    PerformanceReviewGoalDTO selectGoalByWriterIdAndYear(Map<String, Integer> intMap);
}
