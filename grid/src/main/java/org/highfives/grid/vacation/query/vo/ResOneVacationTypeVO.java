package org.highfives.grid.vacation.query.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.highfives.grid.vacation.query.dto.VacationTypeDTO;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ResOneVacationTypeVO {
    private int statusCode;
    private String message;
    private String href;
    private VacationTypeDTO result;
}
