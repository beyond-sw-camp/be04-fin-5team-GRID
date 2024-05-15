package org.highfives.grid.review.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.review.query.dto.ReviewHistoryAndScoreDTO;

@Mapper
public interface ReviewMapper {


    ReviewHistoryAndScoreDTO findHistoryAndScoreById(int historyId);

}
