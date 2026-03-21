package com.MPS.momer_payments_platform.service;

import com.MPS.momer_payments_platform.domain.Account;
import com.MPS.momer_payments_platform.domain.Enums.AccountStatus;
import com.MPS.momer_payments_platform.domain.Enums.MatchResult;
import com.MPS.momer_payments_platform.domain.Payees;
import com.MPS.momer_payments_platform.repository.AccountRepository;
import com.MPS.momer_payments_platform.repository.PayeeRepository;
import com.MPS.momer_payments_platform.api.dto.Payees.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class PayeeService {
    private final AccountRepository accountRepo;
    private final PayeeRepository payeeRepo;

    public PayeeService(AccountRepository accountRepo, PayeeRepository payeeRepo) {
        this.accountRepo = accountRepo;
        this.payeeRepo = payeeRepo;

    }
    public record VopResult(
            String requestedName,
            String actualName,
            String matchResult,
            BigDecimal confidenceScore
    ) {}

    @Transactional
    public PayeeResponse createPayee(CreatePayeeRequest createPayeeRequest){

        Account owner = getOwnerAccountOrThrow(createPayeeRequest.ownerAccountId());
        Account receiver = getReceiverAccountOrThrow(createPayeeRequest.receiverAccountNumber(), createPayeeRequest.receiverSortCode());

        validateAccountIsActive(owner, "Owner");
        validateAccountIsActive(receiver,"Receiver");

        // TODO: call VoP service via Orchestrator
        // For now hardcode a mock result to unblock development
        VopResult mockResult = new VopResult(
                createPayeeRequest.displayName(),
                "John Smith",
                "EXACT_MATCH",
                BigDecimal.valueOf(1.0)
        );
        Instant now = Instant.now();

        Payees payee  = buildAndSavePayees(owner,receiver,mockResult,now);

        return mapToPayeeResponse(payee);
    }
    @Transactional
    public PayeeResponse updatePayeeDisplayName(UpdatePayeeRequest updatePayeeRequest){

        Payees updatedPayee = getOwnerPayeeOrThrow(updatePayeeRequest.ownerAccountID(),updatePayeeRequest.receiverAccountID());
        updatedPayee.setDisplayName(updatePayeeRequest.updatedReceiverName());
        payeeRepo.save(updatedPayee);

        return mapToPayeeResponse(updatedPayee);
    }
    public void deletePayee(UUID ownerAccountId, UUID receiverAccountId){
        Payees payee = getOwnerPayeeOrThrow(ownerAccountId,receiverAccountId);
        payeeRepo.delete(payee);
    }
    private PayeeResponse mapToPayeeResponse(Payees payee) {
        return new PayeeResponse(
                payee.getPayeeId(),
                payee.getOwnerAccount().getAccountId(),
                payee.getReceiverAccount().getAccountId(),
                payee.getReceiverAccount().getAccountNumber(),
                payee.getReceiverAccount().getSortCode(),
                payee.getMatchResult(),
                payee.getConfidenceScore(),
                payee.getDisplayName(),
                payee.getVerifiedName(),
                payee.getVerifiedAt()
        );
    }




    private Payees buildAndSavePayees(Account owner, Account receiver, VopResult vopResult, Instant now) {


        Payees payees = Payees.builder()
                .ownerAccount(owner)
                .receiverAccount(receiver)
                .matchResult(MatchResult.valueOf(vopResult.matchResult))
                .confidenceScore(vopResult.confidenceScore)
                .verifiedName(vopResult.actualName())
                .verifiedAt(now)
                .displayName(receiver.getAccountName())
                .build();

        return payeeRepo.save(payees);
    }

    public Account getOwnerAccountOrThrow(UUID accountID) {
        return accountRepo.findById(accountID)
                .orElseThrow(() -> new IllegalArgumentException("Owner account does not exist cannot link"));
    }
    public Payees getOwnerPayeeOrThrow(UUID ownerAccountId,UUID receiverAccountId) {
        return payeeRepo.findByOwnerAccountAccountIdAndReceiverAccountAccountId(ownerAccountId,receiverAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Payee contact does not exist cannot link"));
    }
    public Account getReceiverAccountOrThrow(String accountNumber, String sortCode) {
        return accountRepo.findByAccountNumberAndSortCode(accountNumber, sortCode)
                .orElseThrow(() -> new IllegalArgumentException("Receiver account does not exist"));
    }
    public void validateAccountIsActive(Account account, String role) {
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException(role + " account not active. Payee cannot be created");
        }
    }
    public List<PayeeResponse> getAllPayeesByOwnerAccountId(UUID ownerAccountId){
        List<Payees> payees = payeeRepo.findAllByOwnerAccountAccountId(ownerAccountId);
        return payees.stream()
                .map(this::mapToPayeeResponse)
                .toList();
    }


}
