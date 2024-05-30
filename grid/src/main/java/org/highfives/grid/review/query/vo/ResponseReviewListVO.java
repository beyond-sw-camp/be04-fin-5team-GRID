package org.highfives.grid.review.query.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.highfives.grid.review.query.dto.ReviewListDTO;

@Getter
@Setter
@NoArgsConstructor
public class ResponseReviewListVO {

    private int statusCode;
    private String message;
    private String href;
    private ReviewListDTO result;

    @Builder
    public ResponseReviewListVO(int statusCode, String message, String href, ReviewListDTO result) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.result = result;
    }
}
