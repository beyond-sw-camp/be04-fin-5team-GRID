package org.highfives.grid.work.query.vo;

import lombok.Builder;
import lombok.Data;
import org.highfives.grid.work.query.dto.RwApprovalDTO;

import java.util.List;

@Data
public class ResponseRwListVO {

    private String message;

    private List<RwApprovalDTO> rwList;

    @Builder
    public ResponseRwListVO(String message, List<RwApprovalDTO> rwList) {
        this.message = message;
        this.rwList = rwList;
    }
}
