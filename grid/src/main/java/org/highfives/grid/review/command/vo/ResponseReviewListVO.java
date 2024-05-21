package org.highfives.grid.review.command.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.highfives.grid.review.command.dto.ReviewDTO;
import org.highfives.grid.review.command.dto.ReviewListDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponseReviewListVO {

    private int statusCode;
    private String message;
    private String href;
    private List<ReviewListDTO> result;

    @Builder
    public ResponseReviewListVO(int statusCode, String message, String href, List<ReviewListDTO> result) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.result = result;
    }
}
