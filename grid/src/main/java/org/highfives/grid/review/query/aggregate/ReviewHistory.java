package org.highfives.grid.review.query.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.highfives.grid.review.command.aggregate.ReviewStatus;

@Getter
@RequiredArgsConstructor
@ToString
public class ReviewHistory {

    private int id;

    private String content;

    private ReviewStatus reviewStatus;

    private String writeTime;

    private int year;

    private int quarter;

    private int reviewerId;

    @Column(name = "reviewee_id", nullable = false)
    private int revieweeId;

}
