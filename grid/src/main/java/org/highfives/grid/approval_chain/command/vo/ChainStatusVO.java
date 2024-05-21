package org.highfives.grid.approval_chain.command.vo;

import lombok.*;
import org.highfives.grid.approval_chain.command.aggregate.ChainStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChainStatusVO {

    // 프론트에서 직접 입력
    private int typeId;

    private int chainId;

    private int approvalId;

    // 프론트에서 직접 입력
    private ChainStatus chainStatus;
}
