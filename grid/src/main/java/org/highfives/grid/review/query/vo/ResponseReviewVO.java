package org.highfives.grid.review.query.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.highfives.grid.review.query.dto.ReviewDTO;

@Getter
@Setter
@NoArgsConstructor
public class ResponseReviewVO {
    private int statusCode;
    private String message;
    private String href;
    private ReviewDTO result;

    @Builder
    public ResponseReviewVO(int statusCode, String message, String href, ReviewDTO result) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.result = result;
    }
}
