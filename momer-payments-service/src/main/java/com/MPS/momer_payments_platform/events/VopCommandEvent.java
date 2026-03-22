package com.MPS.momer_payments_platform.events;

public record VopCommandEvent(
        String correlationId,
        String requestedName,
        String actualName
) {}
