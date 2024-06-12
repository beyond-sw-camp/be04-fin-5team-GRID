package org.highfives.grid.user.query.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LeaderInfoDTO {

    private String teamName;
    private int teamLeaderId;
    private String teamLeaderName;
    private String teamLeaderPosition;
    private String depName;
    private int depLeaderId;
    private String depLeaderName;
    private String depLeaderPosition;
}
