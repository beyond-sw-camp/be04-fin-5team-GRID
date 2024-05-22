package org.highfives.grid.vacation.query.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class VacationDocument {
    private int id;
    private String originName;
    private String renameName;
    private String path;
    private int approvalId;

}
