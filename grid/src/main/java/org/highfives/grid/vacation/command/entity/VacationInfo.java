package org.highfives.grid.vacation.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name = "vacation_info")
@Getter
@ToString
@RequiredArgsConstructor
public class VacationInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "vacation_num")
    private double vacationNum;

    @Column(nullable = false, name = "add_time")
    private String addTime;

    @Column(nullable = false, name = "end_time")
    private String endTime;

    @Column(nullable = false, name = "employee_id")
    private int employeeId;

    @Column(nullable = false, name = "type_id")
    private int typeId;

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public void setVacationNum(double vacationNum) {
        this.vacationNum = vacationNum;
    }

    @Builder
    public VacationInfo(int id, double vacationNum, String addTime, String endTime, int employeeId, int typeId) {
        this.id = id;
        this.vacationNum = vacationNum;
        this.addTime = addTime;
        this.endTime = endTime;
        this.employeeId = employeeId;
        this.typeId = typeId;
    }
}
