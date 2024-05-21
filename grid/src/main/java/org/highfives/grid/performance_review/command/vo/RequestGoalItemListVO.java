package org.highfives.grid.performance_review.command.vo;

import lombok.*;
import org.highfives.grid.performance_review.command.dto.RequestGoalItemDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class RequestGoalItemListVO {
    // 프론트에서 목표 id와 목표 항목(goalId만 따로) 전송
    private Integer goalId;             // 목표 id
    private String approvalStatus;      // 목표 승인 상태
    private List<RequestGoalItemDTO> goalItemList;
}
