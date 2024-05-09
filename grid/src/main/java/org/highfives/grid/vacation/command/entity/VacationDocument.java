package org.highfives.grid.vacation.command.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

@Data
@Entity
@DynamicInsert
@Table(name = "vacation_document")
public class VacationDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "origin_name")
    private String originName;

    @Column(nullable = false, name = "rename_name")
    private String renameName;

    @Column(nullable = false, name = "path")
    private String path;

    @Column(nullable = false, name = "approval_id")
    private int approvalId;

}
