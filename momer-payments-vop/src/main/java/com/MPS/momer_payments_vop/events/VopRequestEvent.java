package com.MPS.momer_payments_vop.events;

public record VopRequestEvent(
        String requestedName,
        String actualName
) {}
