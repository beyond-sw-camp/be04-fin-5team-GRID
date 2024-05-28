package org.highfives.grid.vacation.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "vacation_type")
@Getter
@ToString
@RequiredArgsConstructor
public class VacationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "type_name")
    private String typeName;

    @Column(name = "vacation_num")
    private String vacationNum;

    @Column(name = "date_of_use")
    private String dateOfUse;

    @Column(name = "vacation_explain")
    private String vacationExplain;

    public void setVacationNum(String vacationNum) {
        this.vacationNum = vacationNum;
    }

    public void setDateOfUse(String dateOfUse) {
        this.dateOfUse = dateOfUse;
    }

    public void setVacationExplain(String vacationExplain) {
        this.vacationExplain = vacationExplain;
    }

    @Builder
    public VacationType(int id, String typeName, String vacationNum, String dateOfUse, String vacationExplain) {
        this.id = id;
        this.typeName = typeName;
        this.vacationNum = vacationNum;
        this.dateOfUse = dateOfUse;
        this.vacationExplain = vacationExplain;
    }
}
