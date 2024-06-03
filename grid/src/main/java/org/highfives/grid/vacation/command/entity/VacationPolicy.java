package org.highfives.grid.vacation.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name = "vacation_policy")
@Getter
@ToString
@RequiredArgsConstructor
public class VacationPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "content")
    private String content;

    @Column(nullable = false, name = "type_id")
    private int typeId;

    public void setContent(String content) {
        this.content = content;
    }

    @Builder
    public VacationPolicy(int id, String content, int typeId) {
        this.id = id;
        this.content = content;
        this.typeId = typeId;
    }
}
