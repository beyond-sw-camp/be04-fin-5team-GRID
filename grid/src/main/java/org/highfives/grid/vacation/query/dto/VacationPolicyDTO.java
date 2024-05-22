package org.highfives.grid.vacation.query.dto;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class VacationPolicyDTO {

    private int id;
    private String content;
    private int typeId;

    @Builder
    public VacationPolicyDTO(int id, String content, int typeId) {
        this.id = id;
        this.content = content;
        this.typeId = typeId;
    }
}
