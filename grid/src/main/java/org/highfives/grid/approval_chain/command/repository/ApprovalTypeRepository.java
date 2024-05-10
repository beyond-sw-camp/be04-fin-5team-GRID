package org.highfives.grid.approval_chain.command.repository;

import org.highfives.grid.approval_chain.command.aggregate.ApprovalType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalTypeRepository extends JpaRepository<ApprovalType, Integer> {

}
