package org.highfives.grid.performance_review.command.vo;

import lombok.*;
import org.highfives.grid.performance_review.command.dto.RequestGoalItemDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class RequestGoalVO {
    // 목표 저장
    // 프론트에서 목표 id와 목표 항목(goalId 제외) 전송
    private Integer id;             // 목표 id
    private List<RequestGoalItemDTO> goalItemList;
}
