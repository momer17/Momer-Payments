package com.MPS.momer_payments_platform.service;

import com.MPS.momer_payments_platform.domain.Account;
import com.MPS.momer_payments_platform.domain.Enums.AccountStatus;
import com.MPS.momer_payments_platform.domain.Enums.TransactionStatus;
import com.MPS.momer_payments_platform.domain.Transaction;
import com.MPS.momer_payments_platform.repository.AccountRepository;
import com.MPS.momer_payments_platform.repository.TransactionRepository;
import com.MPS.momer_payments_platform.api.dto.Payment.CreatePaymentRequest;
import com.MPS.momer_payments_platform.api.dto.Payment.PaymentResponse;
import com.MPS.momer_payments_platform.api.dto.Transaction.TransactionResponse;
import com.MPS.momer_payments_platform.util.PaymentReferenceGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
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

        Transaction transaction = buildAndSaveTransaction(paymentRequest, sender, receiver);

        return mapToPaymentResponse(transaction);
    }

    public Transaction buildAndSaveTransaction(CreatePaymentRequest paymentRequest, Account sender, Account receiver) {

        Transaction transaction = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(paymentRequest.amount())
                .currency(paymentRequest.currency())
                .transactionStatus(TransactionStatus.INITIATED)
                .idempotencyKey(paymentRequest.idempotencyKey())
                .paymentDescription(paymentRequest.paymentDescription())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .paymentReferenceNumber(PaymentReferenceGenerator.generatePaymentReference())
                .build();

        return transactionRepo.save(transaction);
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
    private void validateIdempotencyKey(String idempotencyKey) {
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



    public List<TransactionResponse> getTransactionHistory(UUID accountId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        List<Transaction> transactions = transactionRepo.findAllBySenderOrReceiver(account, account);
        return transactions.stream()
                .map(this::mapToTransactionResponse)
                .toList();
    }

    public TransactionResponse getTransactionById(UUID transactionId) {
        Transaction transaction = transactionRepo.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        return mapToTransactionResponse(transaction);
    }

    public List<PaymentResponse> getAllPayments() {
        List<Transaction> transactions = transactionRepo.findAll();
        return transactions.stream()
                .map(this::mapToPaymentResponse)
                .toList();
    }

    public PaymentResponse getPaymentById(UUID paymentId) {
        Transaction transaction = transactionRepo.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        return mapToPaymentResponse(transaction);
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getTransactionId(),
                transaction.getPaymentReferenceNumber(),
                transaction.getSender().getAccountId(),
                transaction.getSender().getAccountNumber(),
                transaction.getSender().getSortCode(),
                transaction.getReceiver().getAccountId(),
                transaction.getReceiver().getAccountNumber(),
                transaction.getReceiver().getSortCode(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getPaymentDescription(),
                transaction.getTransactionStatus(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }

    private PaymentResponse mapToPaymentResponse(Transaction transaction) {
        return new PaymentResponse(
                transaction.getTransactionId(),
                transaction.getSender().getAccountId(),
                transaction.getSender().getAccountNumber(),
                transaction.getSender().getSortCode(),
                transaction.getReceiver().getAccountId(),
                transaction.getReceiver().getAccountNumber(),
                transaction.getReceiver().getSortCode(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getTransactionStatus(),
                transaction.getPaymentDescription(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }

}
