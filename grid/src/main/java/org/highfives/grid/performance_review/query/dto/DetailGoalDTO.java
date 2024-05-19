package org.highfives.grid.performance_review.query.dto;


import lombok.Data;

import java.util.List;

@Data
public class DetailGoalDTO {
    private int id;
    private int year;
    private String reviewName;
    private String approvalStatus;
    private WriterDTO writer;
    private String writeTime;
    private ApproverDTO approver;
    private String approvalTime;
    private List<GoalItemDTO> goalItemList;
}
