package com.MPS.momer_payments_platform.domain;

import com.MPS.momer_payments_platform.domain.Enums.MatchResult;
import com.MPS.momer_payments_platform.domain.Enums.VerificationStatus;
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

    @Column(nullable = false)
    private String correlationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus;

    private double confidenceScore;

    private String displayName;

    private String verifiedName;

    private Instant verifiedAt;


    @PrePersist
    public void initExternalId() {
        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }
    }
}
