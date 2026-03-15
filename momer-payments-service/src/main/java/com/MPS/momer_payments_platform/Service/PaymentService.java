package com.MPS.momer_payments_platform.Service;

import com.MPS.momer_payments_platform.Domain.Account;
import com.MPS.momer_payments_platform.Domain.Enums.AccountStatus;
import com.MPS.momer_payments_platform.Domain.Enums.TransactionStatus;
import com.MPS.momer_payments_platform.Domain.Transaction;
import com.MPS.momer_payments_platform.Repo.AccountRepository;
import com.MPS.momer_payments_platform.Repo.TransactionRepository;
import com.MPS.momer_payments_platform.api.dto.Payment.CreatePaymentRequest;
import com.MPS.momer_payments_platform.api.dto.Payment.PaymentResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class PaymentService {
    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;

    public PaymentService(AccountRepository accountRepo,TransactionRepository transactionRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }
    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest paymentRequest) {
        validateIdempotencyKey(paymentRequest.idempotencyKey());

        Account receiver = getReceiverAccountOrThrow(paymentRequest.receiverAccountNumber(), paymentRequest.receiverSortCode()
        );
        validateAccountIsActive(receiver, "Receiver");

        Account sender = getSenderAccountOrThrowWithLock(paymentRequest.senderAccountId());
        validateAccountIsActive(sender, "Sender");
        validateSufficientFunds(sender.getAccountBalance(),paymentRequest.amount());


        applyCredit(receiver, paymentRequest.amount());
        applyDebit(sender, paymentRequest.amount());
        TransactionStatus status = TransactionStatus.INITIATED;

        Instant now = Instant.now();

        Transaction transaction = buildAndSaveTransaction(paymentRequest, sender, receiver, status, now);

        return buildCreatePaymentResponse(paymentRequest, sender, receiver, transaction, now);
    }

    public Transaction buildAndSaveTransaction(CreatePaymentRequest paymentRequest, Account sender, Account receiver, TransactionStatus status, Instant now) {

        Transaction transaction = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(paymentRequest.amount())
                .currency(paymentRequest.currency())
                .transactionStatus(status)
                .idempotencyKey(paymentRequest.idempotencyKey())
                .paymentDescription(paymentRequest.paymentDescription())
                .createdAt(now)
                .updatedAt(now)
                .build();

        return transactionRepo.save(transaction);
    }
    private PaymentResponse buildCreatePaymentResponse(CreatePaymentRequest paymentRequest, Account sender, Account receiver, Transaction transaction, Instant now) {
        return new PaymentResponse(
                transaction.getTransactionId(),
                sender.getAccountId(),
                sender.getAccountNumber(),
                sender.getSortCode(),
                receiver.getAccountId(),
                receiver.getAccountNumber(),
                receiver.getSortCode(),
                paymentRequest.amount(),
                paymentRequest.currency(),
                transaction.getTransactionStatus(),
                paymentRequest.paymentDescription(),
                now,
                now
        );
    }
    private void validateAccountIsActive(Account account, String role) {
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException(role + " account not active. Payment cannot be made");
        }
    }
    public Account getSenderAccountOrThrowWithLock(UUID senderAccountID) {
        return accountRepo.findByIdWithLock(senderAccountID)
                .orElseThrow(() -> new IllegalArgumentException("Sender account does not exist"));
    }
    public Account getReceiverAccountOrThrow(String accountNumber, String sortCode) {
        return accountRepo.findByAccountNumberAndSortCode(accountNumber, sortCode)
                .orElseThrow(() -> new IllegalArgumentException("Receiver account does not exist"));
    }
    private void validateIdempotencyKey(UUID idempotencyKey) {
        if (idempotencyKey == null) {
            throw new IllegalArgumentException("Idempotency key must be provided");
        }

        boolean alreadyUsed = transactionRepo.existsByIdempotencyKey(idempotencyKey);
        if (alreadyUsed) {
            throw new IllegalStateException("Idempotency key has already been used");
        }
    }
    private void applyCredit(Account account, BigDecimal creditAmount){
        account.setAccountBalance(account.getAccountBalance().add(creditAmount));
        accountRepo.save(account);
    }
    private void applyDebit(Account account, BigDecimal debitAmount){
        account.setAccountBalance(account.getAccountBalance().subtract(debitAmount));
        accountRepo.save(account);
    }
    private void validateSufficientFunds(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }


    /* public boolean isSavedPayee(UUID ownerAccountId, UUID receiverAccountId){
        return payeeRepo.existsByOwnerAccountIdAndReceiverAccountId(ownerAccountId,receiverAccountId);
    }*/

}
