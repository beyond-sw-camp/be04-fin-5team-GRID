package org.highfives.grid.work.query.vo;

import lombok.*;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ResponseOvertimeListVO {
    private String message;

    private List<OvertimeApprovalDTO> overTimeList;
}
