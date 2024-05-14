package org.highfives.grid.department.command.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.highfives.grid.department.command.dto.DepartmentDTO;

import java.util.List;

@Getter
@NoArgsConstructor

public class ResponseDepartmentVO {

    private int statusCode;

    private String message;

    private String href;

    private DepartmentDTO result;



    @Builder
    public ResponseDepartmentVO(int statusCode, String message, String href, DepartmentDTO result) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.result = result;
    }

}
