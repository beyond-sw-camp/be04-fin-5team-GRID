package org.highfives.grid.vacation.query.vo;

import lombok.*;
import org.highfives.grid.vacation.query.dto.VacationInfoDTO;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ResVacationInfoVO {

    private int statusCode;
    private String message;
    private String href;
    private List<VacationInfoDTO> result;

}
