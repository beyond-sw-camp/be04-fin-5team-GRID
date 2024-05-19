package org.highfives.grid.approval_chain.command.repository;

import org.highfives.grid.approval_chain.command.aggregate.OApprovalChain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OApprovalChainRepository extends JpaRepository<OApprovalChain, Integer> {
}
