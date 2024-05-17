package org.highfives.grid.review.query.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.highfives.grid.review.query.dto.ReviewHistoryAndScoreDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponseReviewHistoryAndScoreVO {
    private int statusCode;
    private String message;
    private String href;
    private List<ReviewHistoryAndScoreDTO> result;

    @Builder
    public ResponseReviewHistoryAndScoreVO(int statusCode, String message, String href, List<ReviewHistoryAndScoreDTO> result) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.result = result;
    }
}
