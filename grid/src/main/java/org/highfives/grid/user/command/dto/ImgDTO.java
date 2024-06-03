package org.highfives.grid.user.command.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImgDTO {

    private int Id;

    private String originName;

    private String renameName;

    private String path;

    private int employee_id;

    private int typeId;
}
