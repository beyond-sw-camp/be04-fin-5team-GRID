package org.highfives.grid.vacation.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name = "vacation_history")
@Getter
@ToString
@RequiredArgsConstructor
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

    @Column(name = "change_num")
    private double changeNum;

    @Builder
    public VacationHistory(int id, String changeTime, String changeReason, int typeId, int changeTypeId, int employeeId, double changeNum) {
        this.id = id;
        this.changeTime = changeTime;
        this.changeReason = changeReason;
        this.typeId = typeId;
        this.changeTypeId = changeTypeId;
        this.employeeId = employeeId;
        this.changeNum = changeNum;
    }
}
