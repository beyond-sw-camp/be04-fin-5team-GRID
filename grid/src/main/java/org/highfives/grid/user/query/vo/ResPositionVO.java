package org.highfives.grid.user.query.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.highfives.grid.user.query.dto.PositionDTO;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResPositionVO {
    private int statusCode;

    private String message;

    private String href;

    private List<PositionDTO> result;
}
