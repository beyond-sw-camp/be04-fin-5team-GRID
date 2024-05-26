package org.highfives.grid.work.command.vo;

import lombok.*;
import org.highfives.grid.work.common.dto.AdTimeDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ResponseAdTimeVO {
    private int statusCode;
    private String message;
    private String href;

    private AdTimeDTO adTimeDTO;
}
