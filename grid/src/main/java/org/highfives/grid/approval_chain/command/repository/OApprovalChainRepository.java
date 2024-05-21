package org.highfives.grid.approval_chain.command.repository;

import org.highfives.grid.approval_chain.command.aggregate.OApprovalChain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OApprovalChainRepository extends JpaRepository<OApprovalChain, Integer> {

    @Query("SELECT o FROM OApprovalChain o WHERE o.approvalId = :approvalId")
    OApprovalChain findByApprovalId(@Param("approvalId") int approvalId);
}
