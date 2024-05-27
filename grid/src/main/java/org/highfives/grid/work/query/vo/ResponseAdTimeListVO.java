package org.highfives.grid.work.query.vo;

import lombok.*;
import org.highfives.grid.work.common.dto.AdTimeDTO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ResponseAdTimeListVO {
    private int statusCode;
    private String message;
    private String href;

    private List<AdTimeDTO> adTimeDTOList;
}
