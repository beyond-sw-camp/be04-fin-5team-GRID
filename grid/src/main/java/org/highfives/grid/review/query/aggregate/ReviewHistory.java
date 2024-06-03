package org.highfives.grid.review.query.aggregate;

import jakarta.persistence.*;
import lombok.*;
import org.highfives.grid.review.query.aggregate.ReviewStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewHistory {

    private int id;

    private String content;

    private ReviewStatus reviewStatus;

    private String writeTime;

    private int year;

    private int quarter;

    private int reviewerId;

    private int revieweeId;

}
