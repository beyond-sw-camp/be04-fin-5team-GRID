package org.highfives.grid.work.query.vo;

import lombok.*;
import org.highfives.grid.work.query.dto.QueryAdTimeDTO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseAdTimeListVO {
    private String message;

    private List<QueryAdTimeDTO> queryAdTimeDTOList;
}
