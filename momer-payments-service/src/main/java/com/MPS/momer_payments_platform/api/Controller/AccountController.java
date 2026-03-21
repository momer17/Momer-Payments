package com.MPS.momer_payments_platform.api.Controller;

import com.MPS.momer_payments_platform.domain.Account;
import com.MPS.momer_payments_platform.service.AccountService;
import com.MPS.momer_payments_platform.api.dto.Account.AccountResponse;
import com.MPS.momer_payments_platform.api.dto.Account.CreateAccountRequest;
import com.MPS.momer_payments_platform.api.dto.Account.UpdateAccountRequest;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest accountRequest){
        return ResponseEntity.ok(accountService.createAccount(accountRequest));
    }

    @PatchMapping ("/{accountId}/name")
    public ResponseEntity<AccountResponse> updateAccountDisplayName(@PathVariable UUID accountId, @Valid @RequestBody UpdateAccountRequest updateAccountRequest){
        return ResponseEntity.ok(accountService.updateAccountDisplayName(updateAccountRequest));
    }

    @PatchMapping("/{accountId}/fund")
    public ResponseEntity<AccountResponse> fundAccount(@PathVariable UUID accountId, @RequestParam BigDecimal amount){
        return ResponseEntity.ok(accountService.fundAccount(accountId,amount));
    }

    @PatchMapping("/{accountId}")
    public ResponseEntity<AccountResponse> closeAccount(@PathVariable UUID accountId){
        return ResponseEntity.ok(accountService.closeAccount(accountId));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountByID(@PathVariable UUID accountId){
        return ResponseEntity.ok(accountService.getAccountOrThrow(accountId));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }






}
