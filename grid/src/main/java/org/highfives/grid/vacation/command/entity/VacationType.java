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

    @Column(nullable = false, name = "time_check")
    private String timeCheck;

    @Column(nullable = false, name = "vacation_time")
    private int vacationTime;

    @Column(name = "vacation_num")
    private String vacationNum;

    @Column(name = "date_of_use")
    private String dateOfUse;

    @Column(name = "vacation_explain")
    private String vacationExplain;

    @Column(nullable = false, name = "use_yn")
    private String useYn;

    public void setVacationNum(String vacationNum) {
        this.vacationNum = vacationNum;
    }

    public void setDateOfUse(String dateOfUse) {
        this.dateOfUse = dateOfUse;
    }

    public void setVacationExplain(String vacationExplain) {
        this.vacationExplain = vacationExplain;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public void setTimeCheck(String timeCheck) {
        this.timeCheck = timeCheck;
    }

    public void setVacationTime(int vacationTime) {
        this.vacationTime = vacationTime;
    }

    @Builder
    public VacationType(int id, String typeName, String timeCheck, int vacationTime, String vacationNum, String dateOfUse, String vacationExplain, String useYn) {
        this.id = id;
        this.typeName = typeName;
        this.timeCheck = timeCheck;
        this.vacationTime = vacationTime;
        this.vacationNum = vacationNum;
        this.dateOfUse = dateOfUse;
        this.vacationExplain = vacationExplain;
        this.useYn = useYn;
    }
}