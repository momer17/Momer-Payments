package com.MPS.momer_payments_platform.api.dto.Account;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeleteAccountRequest(
        @NotNull UUID ownerAccountID
) {}
