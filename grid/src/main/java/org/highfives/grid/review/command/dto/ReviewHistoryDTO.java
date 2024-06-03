package org.highfives.grid.review.command.dto;

import lombok.*;
import org.highfives.grid.review.command.aggregate.ReviewStatus;

@Getter
@RequiredArgsConstructor
@ToString
@Setter
public class ReviewHistoryDTO {
    private int id;

    private String content;

    private String reviewStatus;

    private String writeTime;

    private int year;

    private int quarter;

    private int reviewerId;

    private int revieweeId;

    @Builder
    public ReviewHistoryDTO(int id, String content, String reviewStatus, String writeTime, int year, int quarter, int reviewerId, int revieweeId) {
        this.id = id;
        this.content = content;
        this.reviewStatus = reviewStatus;
        this.writeTime = writeTime;
        this.year = year;
        this.quarter = quarter;
        this.reviewerId = reviewerId;
        this.revieweeId = revieweeId;
    }
}
