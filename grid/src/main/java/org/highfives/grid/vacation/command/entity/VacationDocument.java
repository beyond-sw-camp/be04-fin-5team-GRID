package org.highfives.grid.vacation.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name = "vacation_document")
@Getter
@ToString
@RequiredArgsConstructor
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

    @Builder
    public VacationDocument(int id, String originName, String renameName, String path, int approvalId) {
        this.id = id;
        this.originName = originName;
        this.renameName = renameName;
        this.path = path;
        this.approvalId = approvalId;
    }
}
