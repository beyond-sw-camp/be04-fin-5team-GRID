package org.highfives.grid.approval_chain.command.vo;

import lombok.*;
import org.highfives.grid.approval_chain.common.dto.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResApprovalChainVO {

    private int statusCode;

    private String message;

    private String href;

    private BTApprovalChainDTO btChainResult;

    private OApprovalChainDTO oChainResult;

    private RWApprovalChainDTO rwChainResult;

    private VApprovalChainDTO vChainResult;

    private ChainDTO chainResult;

    private List<ChainDTO> chainResultList;
}
