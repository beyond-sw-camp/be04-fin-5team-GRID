package org.highfives.grid.approval_chain.command.repository;

import org.highfives.grid.approval_chain.command.aggregate.BTApprovalChain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BTApprovalChainRepository extends JpaRepository<BTApprovalChain, Integer> {
}
