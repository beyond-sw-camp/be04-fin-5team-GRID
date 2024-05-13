package org.highfives.grid.work.query.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.highfives.grid.work.query.dto.BtApprovalDTO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ResponseBtListVO {
    private String message;

    private List<BtApprovalDTO> btList;
}
