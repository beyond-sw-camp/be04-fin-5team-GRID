package org.highfives.grid.performance_review.command.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class RequestGoalItemDTO {
    private Integer id;
    private String jobName;
    private String goal;
    private String metric;
    private Integer weight;
    private String plan;
    private String objection;
}
