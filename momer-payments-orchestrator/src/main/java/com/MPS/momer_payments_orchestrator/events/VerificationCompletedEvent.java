package com.MPS.momer_payments_orchestrator.events;

import java.time.Instant;

public record VerificationCompletedEvent(
        String senderAccountNumber,
        String senderSortCode,
        String receiverAccountNumber,
        String receiverSortCode,
        String requestedName,
        String actualName,
        String matchResult,
        Double confidenceScore,
        String sagaId,
        Instant verifiedAt
) {}