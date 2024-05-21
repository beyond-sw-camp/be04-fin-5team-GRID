package org.highfives.grid.review.command.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.highfives.grid.review.command.dto.ReviewListDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponseAddReviewListVO {

    private int statusCode;
    private String message;
    private String href;
    private ReviewListDTO result;

    @Builder
    public ResponseAddReviewListVO(int statusCode, String message, String href, ReviewListDTO result) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.result = result;
    }
}
