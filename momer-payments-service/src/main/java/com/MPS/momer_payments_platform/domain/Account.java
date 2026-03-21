package com.MPS.momer_payments_platform.domain;

import com.MPS.momer_payments_platform.domain.Enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;

    private String accountNumber;

    private String accountName;

    private String sortCode;

    private BigDecimal accountBalance;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @OneToMany(mappedBy = "ownerAccount", fetch = FetchType.LAZY)
    private List<Payees> payees;

    private Instant createdDate;

    private Instant updatedDate;



}





