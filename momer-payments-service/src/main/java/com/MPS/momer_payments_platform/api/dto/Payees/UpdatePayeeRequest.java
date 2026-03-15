package com.MPS.momer_payments_platform.api.dto.Payees;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdatePayeeRequest(
        @NotNull UUID ownerAccountID,
        @NotNull UUID receiverAccountID,
        @NotBlank String updatedReceiverName
) {}
