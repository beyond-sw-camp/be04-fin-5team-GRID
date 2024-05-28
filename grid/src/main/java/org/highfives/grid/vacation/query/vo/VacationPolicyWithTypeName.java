package org.highfives.grid.vacation.query.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class VacationPolicyWithTypeName {
    private int id;
    private String content;
    private int typeId;
    private String typeName;
}
