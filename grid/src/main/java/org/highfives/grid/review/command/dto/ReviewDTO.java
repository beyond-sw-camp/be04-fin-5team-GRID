package org.highfives.grid.review.command.dto;


import lombok.*;

@Getter
@RequiredArgsConstructor
@ToString
@Setter
public class ReviewDTO {

    private int id;

    private int score;

    private int historyId;

    private int reviewId;

    @Builder
    public ReviewDTO(int id, int score, int historyId, int reviewId) {
        this.id = id;
        this.score = score;
        this.historyId = historyId;
        this.reviewId = reviewId;
    }
}
