package org.highfives.grid.user.command.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class EmployeeImg {

    @Id
    private int Id;
    @Column
    private String originName;
    @Column
    private String renameName;
    @Column
    private String path;
    @Column
    private int employee_id;
    @Column
    private int typeId;
}
