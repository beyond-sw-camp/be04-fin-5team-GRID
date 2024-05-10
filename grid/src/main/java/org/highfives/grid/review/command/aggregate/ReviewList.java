package org.highfives.grid.review.command.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="member_review_list")
@Getter
@RequiredArgsConstructor
@ToString
public class ReviewList {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 255,nullable = false)
    private String name;

}
