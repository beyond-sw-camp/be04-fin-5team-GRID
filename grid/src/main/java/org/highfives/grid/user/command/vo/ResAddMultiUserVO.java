package org.highfives.grid.user.command.vo;

import lombok.*;
import org.highfives.grid.user.command.dto.UserDTO;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResAddMultiUserVO {

    private int statusCode;
    private String message;
    private String href;
    private List<UserDTO> result;
}
