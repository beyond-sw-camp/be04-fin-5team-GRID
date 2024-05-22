package org.highfives.grid.vacation.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name = "vacation_change_type")
@Getter
@RequiredArgsConstructor
@ToString
public class VacationChangeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "name")
    private String name;

    @Builder
    public VacationChangeType(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
