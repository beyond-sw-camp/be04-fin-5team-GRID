package org.highfives.grid.user.query.vo;

import lombok.*;
import org.highfives.grid.user.command.aggregate.YN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SimpleInfo {
    // 프로필 사진 저장 경로
    // 이름
    // 사번
    // 직위
    // 직책
    // 연락처(전화번호)
    // 부재중 상태
    // 부재중 내용
    /*동현님 요청으로 추가*/
    // pk
    // 입사일
    private int id;
    private String profilePath;
    private String name;
    private String employeeNumber;
    private String position;
    private String duties;
    private String phoneNumber;
    private YN absenceYn;
    private String absenceContent;
    private String joinTime;
}
