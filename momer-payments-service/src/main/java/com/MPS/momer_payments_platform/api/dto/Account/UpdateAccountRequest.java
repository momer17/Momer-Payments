package com.MPS.momer_payments_platform.api.dto.Account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateAccountRequest(
        @NotBlank String accountName,
        @NotNull UUID ownerAccountID
        ) {}
