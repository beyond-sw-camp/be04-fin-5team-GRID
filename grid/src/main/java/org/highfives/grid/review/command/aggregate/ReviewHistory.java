package org.highfives.grid.review.command.aggregate;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="member_review_history")
@Getter
@RequiredArgsConstructor
@ToString
public class ReviewHistory {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "content",length = 511, nullable = false)
    private String content;

    @Column(name = "review_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    @Column(name = "write_time")
    private String writeTime;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "quarter", nullable = false)
    private int quarter;

    @Column(name = "reviewer_id", nullable = false)
    private int reviewerId;

    @Column(name = "reviewee_id", nullable = false)
    private int revieweeId;

    @Builder
    public ReviewHistory(int id, String content, ReviewStatus reviewStatus, String writeTime
            , int year, int quarter, int reviewerId, int revieweeId) {
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
