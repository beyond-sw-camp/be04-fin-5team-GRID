package org.highfives.grid.approval_chain.command.repository;

import org.highfives.grid.approval_chain.command.aggregate.VApprovalChain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VApprovalChainRepository extends JpaRepository<VApprovalChain, Integer> {

    @Query("SELECT v FROM VApprovalChain v WHERE v.approvalId = :approvalId")
    VApprovalChain findByApprovalId(int approvalId);
}
