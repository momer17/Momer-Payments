package com.MPS.momer_payments_platform.api.dto.Payees;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DeletePayeeRequest(
        @NotNull UUID ownerAccountID,
        @NotNull UUID receiverAccountID
) {}
