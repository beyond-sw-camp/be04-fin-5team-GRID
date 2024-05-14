package org.highfives.grid.review.command.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class ReviewListDTO {
    private int id;

    private String listName;

    @Builder
    public ReviewListDTO(int id, String listName) {
        this.id = id;
        this.listName = listName;
    }
}
