package org.highfives.grid.user.command.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.highfives.grid.user.command.dto.UserDTO;

import java.util.List;

@Getter
@AllArgsConstructor
public class ResUserListVO {

    private int statusCode;
    private String message;
    private String href;
    private List<UserDTO> result;
}
