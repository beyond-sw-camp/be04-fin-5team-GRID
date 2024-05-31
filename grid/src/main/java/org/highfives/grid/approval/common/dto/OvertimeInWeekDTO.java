package org.highfives.grid.approval.common.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OvertimeInWeekDTO {

    String sunday;

    String saturday;

    int requesterId;
}
