package org.highfives.grid.approval.command.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResApprovalVO {

    private int statusCode;

    private String message;

    private String href;

    private BTApprovalDTO result;
}
