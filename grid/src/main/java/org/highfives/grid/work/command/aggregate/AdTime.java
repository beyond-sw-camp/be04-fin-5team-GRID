package org.highfives.grid.work.command.aggregate;

import jakarta.persistence.*;
import lombok.*;

// 엔티티 패키지 따로
@Entity
@Table(name="ad_time")
@RequiredArgsConstructor
@Getter
@ToString
public class AdTime {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "start_time")
    private String startTime;

    @Setter
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
