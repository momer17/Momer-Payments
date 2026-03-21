package com.MPS.momer_payments_platform.domain.Enums;

public enum TransactionStatus {
    //TODO: Implement full payment lifecycle in saga
    INITIATED,
    PENDING_VERIFICATION,
    SETTLED,
    FAILED
}
