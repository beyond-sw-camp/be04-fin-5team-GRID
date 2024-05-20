package org.highfives.grid.approval_chain.command.vo;

import lombok.*;
import org.highfives.grid.approval_chain.common.dto.BTApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.OApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.RWApprovalChainDTO;
import org.highfives.grid.approval_chain.common.dto.VApprovalChainDTO;

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

    @Builder
    public ResApprovalChainVO(int statusCode, String message, String href, BTApprovalChainDTO btChainResult) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.btChainResult = btChainResult;
    }

    @Builder
    public ResApprovalChainVO(int statusCode, String message, String href, OApprovalChainDTO oChainResult) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.oChainResult = oChainResult;
    }

    @Builder
    public ResApprovalChainVO(int statusCode, String message, String href, RWApprovalChainDTO rwChainResult) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.rwChainResult = rwChainResult;
    }

    @Builder
    public ResApprovalChainVO(int statusCode, String message, String href, VApprovalChainDTO vChainResult) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.vChainResult = vChainResult;
    }
}
