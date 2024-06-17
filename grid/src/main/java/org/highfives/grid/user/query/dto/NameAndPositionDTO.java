package org.highfives.grid.user.query.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class NameAndPositionDTO {

    private int id;
    private String name;
    private String positionName;
}
