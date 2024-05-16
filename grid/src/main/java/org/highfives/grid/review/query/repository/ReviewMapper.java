package org.highfives.grid.review.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.review.query.aggregate.ReviewHistoryAndScore;
import org.highfives.grid.review.query.dto.ReviewHistoryAndScoreDTO;

import java.util.List;

@Mapper
public interface ReviewMapper {


    List<ReviewHistoryAndScore> findHistoryAndScoreById(int historyId);
}
