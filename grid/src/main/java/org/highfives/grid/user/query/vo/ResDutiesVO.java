package org.highfives.grid.user.query.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.highfives.grid.user.query.dto.DutiesDTO;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResDutiesVO {
    private int statusCode;

    private String message;

    private String href;

    private List<DutiesDTO> result;
}
