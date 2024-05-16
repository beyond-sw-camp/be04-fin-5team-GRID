package org.highfives.grid.vacation.command.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

@Data
@Entity
@DynamicInsert
@Table(name = "vacation_info")
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
}
