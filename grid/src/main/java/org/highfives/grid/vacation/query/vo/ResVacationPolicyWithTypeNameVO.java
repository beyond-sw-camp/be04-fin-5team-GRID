package org.highfives.grid.vacation.query.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.highfives.grid.vacation.query.dto.VacationPolicyDTO;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ResVacationPolicyWithTypeNameVO {

    private int statusCode;
    private String message;
    private String href;
    private List<VacationPolicyWithTypeName> result;
}
