package com.MPS.momer_payments_platform.domain;

import com.MPS.momer_payments_platform.domain.Enums.MatchResult;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payees")
public class Payees {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID payeeId;

    @ManyToOne
    @JoinColumn(name = "owner_account_Id")
    private Account ownerAccount;

    @ManyToOne
    @JoinColumn(name = "receiver_account_Id")
    private Account receiverAccount;

    @Enumerated(EnumType.STRING)
    private MatchResult matchResult;

    private BigDecimal confidenceScore;

    private String displayName;

    private String verifiedName;

    private Instant verifiedAt;
}
