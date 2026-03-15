package com.MPS.momer_payments_platform.Service;

import com.MPS.momer_payments_platform.Domain.Account;
import com.MPS.momer_payments_platform.Repo.AccountRepository;
import com.MPS.momer_payments_platform.api.dto.Account.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepo;

    public AccountService(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest createAccountRequest){

        validateAccountDoesNotExist(createAccountRequest);

        Instant now = Instant.now();
        Account account = buildAndSaveAccount(createAccountRequest);

        return buildAccountResponse(account);
    }
    @Transactional
    public AccountResponse updateAccountDisplayName(UpdateAccountRequest updateAccountRequest){


        Account updatedAccount = getAccountOrThrow(updateAccountRequest.ownerAccountID());
        updatedAccount.setAccountName(updateAccountRequest.accountName());
        updatedAccount.setUpdatedDate(Instant.now());

        accountRepo.save(updatedAccount);

        return buildAccountResponse(updatedAccount);
    }
    public void deleteAccount(DeleteAccountRequest deleteAccountRequest){
        Account Account = getAccountOrThrow(deleteAccountRequest.ownerAccountID());
        accountRepo.delete(Account);
    }
    private AccountResponse buildAccountResponse(Account Account) {
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
                .build();

        return accountRepo.save(account);
    }

    public Account getAccountOrThrow(UUID accountID) {
        return accountRepo.findById(accountID)
                .orElseThrow(() -> new IllegalArgumentException("Owner account does not exist cannot link"));
    }

    public void validateAccountDoesNotExist(CreateAccountRequest accountRequest) {
        if (accountRepo.existsByAccountNumberAndSortCode(accountRequest.accountNumber(), accountRequest.sortCode())) {
            throw new IllegalArgumentException("Account already exists and cannot be created again");
        }
    }


}



