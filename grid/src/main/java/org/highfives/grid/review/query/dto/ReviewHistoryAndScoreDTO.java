package org.highfives.grid.review.query.dto;

import org.highfives.grid.review.command.aggregate.ReviewStatus;

public class ReviewHistoryAndScoreDTO {

    private int id;

    private String content;

    private ReviewStatus reviewStatus;

    private String writeTime;

    private int year;

    private int quarter;

    private int reviewerId;

    private int revieweeId;

    private int score;


}
