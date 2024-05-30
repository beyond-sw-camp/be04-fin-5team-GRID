package org.highfives.grid.user.command.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="employee_img")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
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
    private int employeeId;
    @Column
    private int typeId;
}
