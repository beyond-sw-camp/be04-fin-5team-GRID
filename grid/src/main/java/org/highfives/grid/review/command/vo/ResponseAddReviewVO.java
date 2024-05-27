package org.highfives.grid.review.command.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.highfives.grid.review.command.dto.ReviewDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponseAddReviewVO {
    private int statusCode;
    private String message;
    private String href;
    private List<ReviewDTO> result;

    @Builder
    public ResponseAddReviewVO(int statusCode, String message, String href, List<ReviewDTO> result) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.result = result;
    }
}
