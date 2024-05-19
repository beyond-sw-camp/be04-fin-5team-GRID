package org.highfives.grid.user.command.vo;

import lombok.Data;

@Data
public class ReqLogin {

    private String email;
    private String pwd;
}
