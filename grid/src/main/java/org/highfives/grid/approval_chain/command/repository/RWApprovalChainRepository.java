package org.highfives.grid.approval_chain.command.repository;

import org.highfives.grid.approval_chain.command.aggregate.RWApprovalChain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RWApprovalChainRepository extends JpaRepository<RWApprovalChain, Integer> {

    @Query("SELECT rw FROM RWApprovalChain rw WHERE rw.approvalId = :approvalId")
    RWApprovalChain findByApprovalId(int approvalId);
}
