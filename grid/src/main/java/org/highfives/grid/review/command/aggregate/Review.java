package org.highfives.grid.review.command.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@Entity
@Table(name="member_review")
@Getter
@RequiredArgsConstructor
@ToString
public class Review {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "history_id", nullable = false)
    private int historyId;

    @Column(name = "review_id", nullable = false)
    private int reviewId;


}
