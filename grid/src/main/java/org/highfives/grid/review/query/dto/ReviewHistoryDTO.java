package org.highfives.grid.review.query.dto;

import lombok.*;
import org.highfives.grid.review.query.aggregate.ReviewStatus;


@Getter
@Setter
@RequiredArgsConstructor
@ToString
@AllArgsConstructor
public class ReviewHistoryDTO {

    private int id;

    private String content;

    private ReviewStatus reviewStatus;

    private String writeTime;

    private int year;

    private int quarter;

    private int reviewerId;

    private int revieweeId;


}
