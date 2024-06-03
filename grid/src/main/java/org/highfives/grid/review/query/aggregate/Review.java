package org.highfives.grid.review.query.aggregate;

import jakarta.persistence.*;
import lombok.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Review {

    private int id;

    private int score;

    private int historyId;

    private int reviewId;


}
