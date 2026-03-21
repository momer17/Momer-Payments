package com.MPS.momer_payments_vop.events;

import java.math.BigDecimal;
import java.time.Instant;

public record VopResponseEvent(
        String requestedName,
        String actualName,
        String MatchResult,
        Double confidenceScore,
        Instant verifiedAt
) {}
