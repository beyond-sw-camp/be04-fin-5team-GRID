package org.highfives.grid.user.command.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.highfives.grid.user.command.dto.UserDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResImgUploadVO {

    private int statusCode;
    private String message;
    private String href;
    private String result;

}
