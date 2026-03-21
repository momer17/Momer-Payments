package com.MPS.momer_payments_orchestrator.events;

public record VerificationRequestedEvent(
        String senderAccountNumber,
        String senderSortCode,
        String receiverAccountNumber,
        String receiverSortCode,
        String requestedName,
        String actualName
) {}