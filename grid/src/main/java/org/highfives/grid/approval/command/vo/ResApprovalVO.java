package org.highfives.grid.approval.command.vo;

import lombok.*;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResApprovalVO {

    private int statusCode;

    private String message;

    private String href;

    private BTApprovalDTO result;
}
