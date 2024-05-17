package org.highfives.grid.approval_chain.common.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApprovalChainDTO {

    private int id;

    private String duty;

    private int priority;

    private int typeId;
}
