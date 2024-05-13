package org.highfives.grid.user.query.vo;

import lombok.*;
import org.highfives.grid.user.query.dto.LeaderInfoDTO;

@Getter
@AllArgsConstructor
@ToString
public class ResFindLeaderVO {
    private int statusCode;
    private String message;
    private String href;
    private LeaderInfoDTO result;
}
