package org.highfives.grid.review.query.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewList {

    private int id;

    private String listName;

}
