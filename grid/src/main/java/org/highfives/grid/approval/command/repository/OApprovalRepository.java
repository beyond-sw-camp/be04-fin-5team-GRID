package org.highfives.grid.approval.command.repository;

import org.highfives.grid.approval.command.aggregate.OvertimeApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OApprovalRepository extends JpaRepository<OvertimeApproval, Integer> {
}
