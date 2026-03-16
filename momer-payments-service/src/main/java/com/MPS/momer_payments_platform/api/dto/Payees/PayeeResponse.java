package com.MPS.momer_payments_platform.api.dto.Payees;

import com.MPS.momer_payments_platform.Domain.Enums.MatchResult;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PayeeResponse(
        UUID payeeId,
        UUID ownerAccountId,
        UUID receiverAccountId,
        String receiverAccountNumber,
        String receiverSortCode,
        MatchResult matchResult,
        BigDecimal confidenceScore,
        String displayName,
        String verifiedName,
        Instant verifiedAt
) {}