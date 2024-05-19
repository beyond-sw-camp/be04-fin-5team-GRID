package org.highfives.grid.approval_chain.command.repository;

import org.highfives.grid.approval_chain.command.aggregate.VApprovalChain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VApprovalChainRepository extends JpaRepository<VApprovalChain, Integer> {
}
