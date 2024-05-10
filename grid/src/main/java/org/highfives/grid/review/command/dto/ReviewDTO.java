package org.highfives.grid.review.command.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class ReviewDTO {

    private int id;

    private int score;

    private int historyId;

    private int reviewId;


}
