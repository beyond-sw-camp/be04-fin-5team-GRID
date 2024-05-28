package org.highfives.grid.approval.query.dto;

import lombok.*;
import org.highfives.grid.approval.command.aggregate.ApprovalStatus;
import org.highfives.grid.approval.command.aggregate.YN;
import org.highfives.grid.user.query.dto.UserDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApprovalEmpDTO {

    private int id;

    private String startTime;

    private String endTime;

    private String content;

    private ApprovalStatus approvalStatus;

    private String writeTime;

    private YN cancelYn;

    private int cancelDocId;

    private int employeeId;

    private String employeeName;

    private int employeeNumber;

    private String destination;

    private String originName;

    private String renameName;

    private String path;

    private int infoId;

    private String vacationType;

    private UserDTO user;
}

