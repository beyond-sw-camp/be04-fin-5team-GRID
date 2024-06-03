package org.highfives.grid.department.command.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.highfives.grid.department.command.dto.DepartmentDTO;

import java.util.List;

@Getter
@NoArgsConstructor
public class ResponseDepartmentListVO {
    private int statusCode;

    private String message;

    private String href;

    private List<DepartmentDTO> result;

    @Builder
    public ResponseDepartmentListVO(int statusCode, String message, String href, List<DepartmentDTO> result) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.result = result;
    }
}
