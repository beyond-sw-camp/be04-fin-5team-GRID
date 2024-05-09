package org.highfives.grid.vacation.command.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

@Data
@Entity
@DynamicInsert
@Table(name = "vacation_policy")
public class VacationPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "content")
    private String content;

    @Column(nullable = false, name = "type_id")
    private int typeId;
}
