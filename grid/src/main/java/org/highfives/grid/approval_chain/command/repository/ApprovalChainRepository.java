package org.highfives.grid.approval_chain.command.repository;

import org.highfives.grid.approval_chain.command.aggregate.ApprovalChain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalChainRepository extends JpaRepository<ApprovalChain, Integer> {

}
