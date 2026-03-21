package com.MPS.momer_payments_platform.service;

import com.MPS.momer_payments_platform.domain.Account;
import com.MPS.momer_payments_platform.domain.Enums.AccountStatus;
import com.MPS.momer_payments_platform.repository.AccountRepository;
import com.MPS.momer_payments_platform.api.dto.Account.AccountResponse;
import com.MPS.momer_payments_platform.api.dto.Account.CreateAccountRequest;
import com.MPS.momer_payments_platform.api.dto.Account.UpdateAccountRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepo;

    public AccountService(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest createAccountRequest){

        validateAccountDoesNotExistOrThrow(createAccountRequest);
        Instant now = Instant.now();
        Account account = buildAndSaveAccount(createAccountRequest);
        return mapToAccountResponse(account);
    }
    @Transactional
    public AccountResponse updateAccountDisplayName(UpdateAccountRequest updateAccountRequest){

        Account updatedAccount = getAccountOrThrow(updateAccountRequest.ownerAccountID());
        updatedAccount.setAccountName(updateAccountRequest.accountName());
        updatedAccount.setUpdatedDate(Instant.now());
        accountRepo.save(updatedAccount);

        return mapToAccountResponse(updatedAccount);
    }
    @Transactional
    public AccountResponse fundAccount(UUID accountId, BigDecimal amount) {
        Account account = accountRepo.findByIdWithLock(accountId)
                .orElseThrow(() -> new IllegalArgumentException("That account does not exist"));
        account.setAccountBalance(account.getAccountBalance().add(amount));
        accountRepo.save(account);

        return mapToAccountResponse(account);
    }


    @Transactional
    public AccountResponse closeAccount(UUID accountId){
        Account account = getAccountOrThrow(accountId);
        account.setAccountStatus(AccountStatus.CLOSED);
        accountRepo.save(account);
        return mapToAccountResponse(account);
    }
    private AccountResponse mapToAccountResponse(Account Account) {
        return new AccountResponse(
                Account.getAccountId(),
                Account.getAccountNumber(),
                Account.getSortCode(),
                Account.getAccountName(),
                Account.getAccountBalance(),
                Account.getAccountStatus(),
                Account.getCreatedDate(),
                Account.getUpdatedDate()
        );
    }


    private Account buildAndSaveAccount(CreateAccountRequest accountRequest) {

        Account account = Account.builder()
                .accountName(accountRequest.accountName())
                .accountNumber(accountRequest.accountNumber())
                .sortCode(accountRequest.sortCode())
                .accountBalance(BigDecimal.ZERO)
                .accountStatus(AccountStatus.ACTIVE)
                .createdDate(Instant.now())
                .updatedDate(Instant.now())
                .build();
        return accountRepo.save(account);
    }

    public Account getAccountOrThrow(UUID accountID) {
        return accountRepo.findById(accountID)
                .orElseThrow(() -> new IllegalArgumentException("Owner account does not exist cannot link"));
    }

    public void validateAccountDoesNotExistOrThrow(CreateAccountRequest accountRequest) {
        if (accountRepo.existsByAccountNumberAndSortCode(accountRequest.accountNumber(), accountRequest.sortCode())) {
            throw new IllegalArgumentException("Account already exists and cannot be created again");
        }
    }

    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = accountRepo.findAll();
        return accounts.stream()
                .map(this::mapToAccountResponse)
                .toList();
    }

}



