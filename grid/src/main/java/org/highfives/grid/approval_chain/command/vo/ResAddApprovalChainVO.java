package org.highfives.grid.approval_chain.command.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.highfives.grid.approval_chain.common.dto.BTApprovalChainDTO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResAddApprovalChainVO {

    private int statusCode;

    private String message;

    private String href;

    private List<BTApprovalChainDTO> result;
}
