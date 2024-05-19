package org.highfives.grid.approval_chain.command.repository;

import org.highfives.grid.approval_chain.command.aggregate.RWApprovalChain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RWApprovalChainRepository extends JpaRepository<RWApprovalChain, Integer> {
}
