package org.highfives.grid.review.query.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
@Setter
public class ReviewDTO {

    private int id;

    private int score;

    private int historyId;

    private int reviewId;

}
