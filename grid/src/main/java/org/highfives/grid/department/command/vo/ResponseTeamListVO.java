package org.highfives.grid.department.command.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.highfives.grid.department.command.dto.TeamDTO;

import java.util.List;

@Getter
@NoArgsConstructor
public class ResponseTeamListVO {

    private int statusCode;

    private String message;

    private String href;

    private List<TeamDTO> result;

    @Builder
    public ResponseTeamListVO(int statusCode, String message, String href, List<TeamDTO> result) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.result = result;
    }
}
