package org.highfives.grid.approval_chain.command.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "approval_chain")
public class ApprovalChain {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "duty")
    private String duty;

    @Column(name = "stage")
    private int stage;

    @Column(name = "type_id")
    private int typeId;
}
