package com.MPS.momer_payments_platform.Service;

import com.MPS.momer_payments_platform.Domain.Account;
import com.MPS.momer_payments_platform.Domain.Enums.AccountStatus;
import com.MPS.momer_payments_platform.Domain.Enums.MatchResult;
import com.MPS.momer_payments_platform.Domain.Payees;
import com.MPS.momer_payments_platform.Repo.AccountRepository;
import com.MPS.momer_payments_platform.Repo.PayeeRepository;
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
        payeeRepo.save(payee);

        return buildPayeeResponse(payee);
    }
    @Transactional
    public PayeeResponse updatePayeeDisplayName(UpdatePayeeRequest updatePayeeRequest){

        Payees updatedPayee = getOwnerPayeeOrThrow(updatePayeeRequest.ownerAccountID(),updatePayeeRequest.receiverAccountID());
        updatedPayee.setDisplayName(updatePayeeRequest.updatedReceiverName());
        payeeRepo.save(updatedPayee);

        return buildPayeeResponse(updatedPayee);
    }
    public void deletePayee(DeletePayeeRequest deletePayeeRequest){
        Payees payee = getOwnerPayeeOrThrow(deletePayeeRequest.ownerAccountID(),deletePayeeRequest.receiverAccountID());
        payeeRepo.delete(payee);
    }
    private PayeeResponse buildPayeeResponse(Payees payee) {
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
                .payeeId(owner.getAccountId())
                .ownerAccount(owner)
                .receiverAccount(receiver)
                .matchResult(MatchResult.valueOf(vopResult.matchResult))
                .confidenceScore(vopResult.confidenceScore)
                .verifiedName(vopResult.actualName())
                .verifiedAt(now)
                .build();

        return payeeRepo.save(payees);
    }

    public Account getOwnerAccountOrThrow(UUID accountID) {
        return accountRepo.findById(accountID)
                .orElseThrow(() -> new IllegalArgumentException("Owner account does not exist cannot link"));
    }
    public Payees getOwnerPayeeOrThrow(UUID ownerAccountId,UUID receiverAccountId) {
        return payeeRepo.findByOwnerAccountIdAndReceiverAccountId(ownerAccountId,receiverAccountId)
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
    public List<Payees> getAllPayeesByOwnerAccountId(UUID ownerAccountId){
        return payeeRepo.findAllByOwnerAccountId(ownerAccountId);
    }


}
