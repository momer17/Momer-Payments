package com.MPS.momer_payments_platform.domain;

import com.MPS.momer_payments_platform.domain.Enums.TransactionStatus;
import com.MPS.momer_payments_platform.domain.Enums.Currency;

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
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    private String idempotencyKey;

    private String paymentReferenceNumber;

    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    private Account receiver;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private String paymentDescription;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private Instant createdAt;

    private Instant updatedAt;

}

