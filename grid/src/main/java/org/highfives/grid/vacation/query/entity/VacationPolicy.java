package org.highfives.grid.vacation.query.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class VacationPolicy {
    private int id;
    private String content;
    private int typeId;
}
