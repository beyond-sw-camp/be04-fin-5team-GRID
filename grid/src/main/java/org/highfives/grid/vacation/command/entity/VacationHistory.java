package org.highfives.grid.vacation.command.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

@Data
@Entity
@DynamicInsert
@Table(name = "vacation_history")
public class VacationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "change_time")
    private String changeTime;

    @Column(nullable = false, name = "change_reason")
    private String changeReason;

    @Column(nullable = false, name = "type_id")
    private int typeId;

    @Column(nullable = false, name = "change_type_id")
    private int changeTypeId;

    @Column(nullable = false, name = "employee_id")
    private int employeeId;
}
