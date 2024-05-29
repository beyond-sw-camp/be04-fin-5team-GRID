package org.highfives.grid.user.command.vo;

import lombok.Data;
import org.highfives.grid.user.command.dto.UserDTO;

import java.util.List;

@Data
public class ResImgUploadVO {

    private int statusCode;
    private String message;
    private String href;
    private List<UserDTO> result;

}
