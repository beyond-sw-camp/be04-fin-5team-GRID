package org.highfives.grid.approval_chain.command.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommentVO {

    private String comment;

    private int chainId;
}
