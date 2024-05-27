package org.highfives.grid.review.command.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.highfives.grid.review.command.dto.ReviewHistoryDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponseAddReviewHistoryVO {

    private int statusCode;
    private String message;
    private String href;
    private List<ReviewHistoryDTO> result;


    @Builder
    public ResponseAddReviewHistoryVO(int statusCode, String message, String href, List<ReviewHistoryDTO> result) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.result = result;
    }
}
