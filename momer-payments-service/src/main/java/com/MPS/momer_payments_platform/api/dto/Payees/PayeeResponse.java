package com.MPS.momer_payments_platform.api.dto.Payees;

import com.MPS.momer_payments_platform.domain.Enums.MatchResult;
import com.MPS.momer_payments_platform.domain.Enums.VerificationStatus;

import java.time.Instant;
import java.util.UUID;

public record PayeeResponse(
        UUID payeeId,
        UUID ownerAccountId,
        UUID receiverAccountId,
        String receiverAccountNumber,
        String receiverSortCode,
        MatchResult matchResult,
        String correlationId,
        VerificationStatus verificationStatus,
        double confidenceScore,
        String displayName,
        String verifiedName,
        Instant verifiedAt

) {}