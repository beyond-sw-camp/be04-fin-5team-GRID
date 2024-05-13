package org.highfives.grid.approval.command.service;

import org.highfives.grid.approval.command.vo.BTApprovalVO;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;

public interface ApprovalService {

    BTApprovalDTO addBTApproval(BTApprovalVO btApprovalVO);
}
