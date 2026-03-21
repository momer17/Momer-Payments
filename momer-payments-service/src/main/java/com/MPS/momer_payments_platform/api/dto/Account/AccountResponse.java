package com.MPS.momer_payments_platform.api.dto.Account;

import com.MPS.momer_payments_platform.domain.Enums.AccountStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AccountResponse(
         UUID accountId,
         String accountNumber,
         String sortCode,
         String accountName,
         BigDecimal accountBalance,
         AccountStatus accountStatus,
         Instant createdDate,
         Instant updatedDate) {}
