package org.highfives.grid.user.query.vo;

import lombok.*;
import org.highfives.grid.user.query.dto.UserDTO;

import java.util.List;

@Getter
@AllArgsConstructor
public class ResFindUserVO {

    private int statusCode;
    private String message;
    private String href;
    private UserDTO result;
}
