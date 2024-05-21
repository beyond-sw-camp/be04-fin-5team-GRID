package org.highfives.grid.approval.command.repository;

import org.highfives.grid.approval.command.aggregate.VacationApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VApprovalRepository extends JpaRepository<VacationApproval, Integer> {
}
