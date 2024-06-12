package org.highfives.grid.review.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.review.query.aggregate.ReviewEmployeeHistory;
import org.highfives.grid.review.query.aggregate.ReviewHistory;
import org.highfives.grid.review.query.aggregate.ReviewHistoryAndScore;
import org.highfives.grid.review.query.aggregate.ReviewList;

import java.util.List;

@Mapper
public interface ReviewMapper {


    List<ReviewHistoryAndScore> findHistoryAndScoreById(int historyId, int revieweeId);

    List<ReviewHistory> findAssignedReviewByReviewerId(int reviewerId);

    List<ReviewHistory> findHistoryList();

    List<ReviewHistory> findMyReviewHistory(int revieweeId);

    ReviewList findReviewList(int id);

    List<ReviewEmployeeHistory> findEmployeesHistory();
}
