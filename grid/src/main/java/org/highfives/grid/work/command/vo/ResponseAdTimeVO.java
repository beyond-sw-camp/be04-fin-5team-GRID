package org.highfives.grid.work.command.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseAdTimeVO {
    private String message;

    private int id;
    private String startTime;
    private String endTime;
    private int employeeId;
}
