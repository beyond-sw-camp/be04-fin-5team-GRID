package org.highfives.grid.performance_review.command.vo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ResponseDelGoalItemVO {
    private int statusCode;
    private String message;
    private String href;
    private int id;
}
