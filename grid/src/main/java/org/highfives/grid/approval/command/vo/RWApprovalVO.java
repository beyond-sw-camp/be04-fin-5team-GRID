package org.highfives.grid.approval.command.vo;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class RWApprovalVO {

    private String startTime;

    private String endTime;

    private String content;

    private String writeTime;

    private int requesterId;

    private String originName;

    private String renameName;

    private String path;
}
