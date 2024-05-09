package org.highfives.grid.vacation.query.entity;

import lombok.Data;

@Data
public class VacationDocument {
    private int id;
    private String originName;
    private String renameName;
    private String path;
    private int approvalId;

}
