package org.highfives.grid.vacation.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table( name = "vacation_type")
@Getter
@ToString
@RequiredArgsConstructor
public class VacationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "type_name")
    private String typeName;

    @Builder
    public VacationType(int id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }
}
