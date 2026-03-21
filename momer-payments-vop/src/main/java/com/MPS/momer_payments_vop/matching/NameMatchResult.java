package com.MPS.momer_payments_vop.matching;

import com.MPS.momer_payments_vop.enums.MatchResult;

public record NameMatchResult(MatchResult matchResult, double confidence) {}