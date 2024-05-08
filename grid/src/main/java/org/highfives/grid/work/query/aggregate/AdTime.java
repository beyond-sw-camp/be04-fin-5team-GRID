package org.highfives.grid.work.query.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="ad_time")
//@NoArgsConstructor
//@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class AdTime {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "employee_id")
    private int employeeId;

    public AdTime(String startTime, int employeeId) {
        this.startTime = startTime;
        this.employeeId = employeeId;
    }

    public AdTime(int employeeId, String endTime) {
        this.employeeId = employeeId;
        this.endTime = endTime;
    }
}
