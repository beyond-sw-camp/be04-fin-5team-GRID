package org.highfives.grid.approval.command.repository;

import org.highfives.grid.approval.command.aggregate.BTApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BTApprovalRepository extends JpaRepository<BTApproval, Integer> {
}
