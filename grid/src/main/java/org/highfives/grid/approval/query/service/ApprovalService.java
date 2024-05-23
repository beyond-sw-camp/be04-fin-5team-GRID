package org.highfives.grid.approval.query.service;

import org.highfives.grid.approval.command.aggregate.BTApproval;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;

public interface ApprovalService {

    public BTApprovalDTO findBTApprovalById(int btApprovalId);
    public void exportToPDF(BTApprovalDTO btApproval, String filePath);
}
