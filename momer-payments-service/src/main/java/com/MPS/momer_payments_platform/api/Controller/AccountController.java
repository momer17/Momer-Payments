package com.MPS.momer_payments_platform.api.Controller;

import com.MPS.momer_payments_platform.Domain.Account;
import com.MPS.momer_payments_platform.Service.AccountService;
import com.MPS.momer_payments_platform.api.dto.Account.AccountResponse;
import com.MPS.momer_payments_platform.api.dto.Account.CreateAccountRequest;
import com.MPS.momer_payments_platform.api.dto.Account.DeleteAccountRequest;
import com.MPS.momer_payments_platform.api.dto.Account.UpdateAccountRequest;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest accountRequest){
        return accountService.createAccount(accountRequest);
    }

    @PutMapping
    public AccountResponse updateAccountDisplayName(@Valid @RequestBody UpdateAccountRequest updateAccountRequest){
        return accountService.updateAccountDisplayName(updateAccountRequest);
    }

    @DeleteMapping
    public void deleteAccount(@Valid @RequestBody DeleteAccountRequest deleteAccountRequest){
        accountService.deleteAccount(deleteAccountRequest);
    }

    @GetMapping
    public Account getAccountByID(@Valid @RequestParam(value = "accountUUID") UUID accountID){
        return accountService.getAccountOrThrow(accountID);
    }





}
