package org.highfives.grid.review.query.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.highfives.grid.review.query.dto.ReviewHistoryDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponseReviewHistoryVO {
    private int statusCode;
    private String message;
    private String href;
    private List<ReviewHistoryDTO> result;

    @Builder
    public ResponseReviewHistoryVO(int statusCode, String message, String href, List<ReviewHistoryDTO> result) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.result = result;
    }
}
