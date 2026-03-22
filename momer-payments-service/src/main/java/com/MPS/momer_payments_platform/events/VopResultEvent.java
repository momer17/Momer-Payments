package com.MPS.momer_payments_platform.events;

import java.time.Instant;

public record VopResultEvent(
        String correlationId,
        String requestedName,
        String actualName,
        String MatchResult,
        Double confidenceScore,
        Instant verifiedAt
) {}
