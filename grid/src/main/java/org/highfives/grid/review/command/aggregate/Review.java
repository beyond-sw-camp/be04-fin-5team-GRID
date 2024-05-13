package org.highfives.grid.review.command.aggregate;

import jakarta.persistence.*;
import lombok.Builder;
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

    @Builder
    public Review(int id, int score, int historyId, int reviewId) {
        this.id = id;
        this.score = score;
        this.historyId = historyId;
        this.reviewId = reviewId;
    }
}
