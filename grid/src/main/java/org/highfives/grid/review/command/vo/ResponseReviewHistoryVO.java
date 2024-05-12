package org.highfives.grid.review.command.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.highfives.grid.review.command.dto.ReviewHistoryDTO;

@Getter
@Setter
@NoArgsConstructor
public class ResponseReviewHistoryVO {

    private int statusCode;
    private String message;
    private String href;
    private ReviewHistoryDTO result;


    @Builder
    public ResponseReviewHistoryVO(int statusCode, String message, String href, ReviewHistoryDTO result) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.result = result;
    }
}
