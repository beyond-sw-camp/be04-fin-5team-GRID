package org.highfives.grid.work.query.vo;

import lombok.Builder;
import lombok.Data;
import org.highfives.grid.approval.common.dto.RWApprovalDTO;

import java.util.List;

@Data
public class ResponseRwListVO {

    private String message;

    private List<RWApprovalDTO> rwList;

    @Builder
    public ResponseRwListVO(String message, List<RWApprovalDTO> rwList) {
        this.message = message;
        this.rwList = rwList;
    }
}
