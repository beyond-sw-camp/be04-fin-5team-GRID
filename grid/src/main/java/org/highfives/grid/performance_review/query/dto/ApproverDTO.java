package org.highfives.grid.performance_review.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApproverDTO {
    private int id;
    private String employeeName;
    private String employeeNumber;
}
