package org.highfives.grid.review.query.service;

import com.github.pagehelper.PageInfo;
import org.highfives.grid.review.query.dto.ReviewEmployeesHistoryDTO;
import org.highfives.grid.review.query.dto.ReviewHistoryAndScoreDTO;
import org.highfives.grid.review.query.dto.ReviewHistoryDTO;
import org.highfives.grid.review.query.dto.ReviewListDTO;

import java.util.List;

public interface ReviewService {


    List<ReviewHistoryAndScoreDTO> findHistoryAndScoreById(int historyId, int revieweeId);

    List<ReviewHistoryDTO> findAssignedReviewByReviewerId(int reviewerId);


    List<ReviewHistoryDTO> findHistoryList();

    List<ReviewHistoryDTO> findMyReviewHistory(int revieweeId);

    ReviewListDTO findReviewList(int id);


    PageInfo<ReviewEmployeesHistoryDTO> findEmployeesHistory(int page, int size);
}
