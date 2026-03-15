package com.MPS.momer_payments_vop;

import java.math.BigDecimal;
import java.util.regex.MatchResult;
//TODO: Replace when VoP Seervice is completed
public record VopResponse(
        String requestedName,
        String actualName,
        MatchResult matchResult,
        BigDecimal confidenceScore) {

}