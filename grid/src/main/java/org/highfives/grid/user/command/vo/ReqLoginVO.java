package org.highfives.grid.user.command.vo;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReqLoginVO {

    private String email;
    private String pwd;
}
