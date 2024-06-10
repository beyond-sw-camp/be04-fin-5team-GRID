package org.highfives.grid.user.command.vo;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReqResetPwdVO {

    private String pwd;
    private String email;
}
