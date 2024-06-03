package org.highfives.grid.approval.command.repository;

import org.highfives.grid.approval.command.aggregate.RWApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RWApprovalRepository extends JpaRepository<RWApproval, Integer> {
}
