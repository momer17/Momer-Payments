package com.MPS.momer_payments_platform.service;

import com.MPS.momer_payments_platform.domain.Account;
import com.MPS.momer_payments_platform.domain.Enums.AccountStatus;
import com.MPS.momer_payments_platform.domain.Enums.MatchResult;
import com.MPS.momer_payments_platform.domain.Enums.VerificationStatus;
import com.MPS.momer_payments_platform.domain.Payees;
import com.MPS.momer_payments_platform.events.VopCommandEvent;
import com.MPS.momer_payments_platform.events.VopResultEvent;
import com.MPS.momer_payments_platform.repository.AccountRepository;
import com.MPS.momer_payments_platform.repository.PayeeRepository;
import com.MPS.momer_payments_platform.api.dto.Payees.*;
import com.MPS.momer_payments_platform.publisher.VopCommandPublisher;

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
    private final VopCommandPublisher vopCommandPublisher;

    public PayeeService(AccountRepository accountRepo, PayeeRepository payeeRepo, VopCommandPublisher vopCommandPublisher) {
        this.accountRepo = accountRepo;
        this.payeeRepo = payeeRepo;
        this.vopCommandPublisher = vopCommandPublisher;
    }


    @Transactional
    public PayeeResponse createPayeeVerificationPending(CreatePayeeRequest createPayeeRequest) {
        Account receiver = getReceiverAccountOrThrow(createPayeeRequest.receiverAccountNumber(), createPayeeRequest.receiverSortCode());
        Account owner = getOwnerAccountOrThrow(createPayeeRequest.ownerAccountId());

        validateAccountIsActive(receiver, "Receiver");
        validateAccountIsActive(owner, "Owner");
        Payees payee  = buildAndSavePendingPayee(owner,receiver);
        vopCommandPublisher.publishVopExecuteCommand(buildVopCommandEvent(payee.getCorrelationId(),createPayeeRequest.requestedName(), receiver.getAccountName()));

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
                payee.getCorrelationId(),
                payee.getVerificationStatus(),
                payee.getConfidenceScore(),
                payee.getDisplayName(),
                payee.getVerifiedName(),
                payee.getVerifiedAt()
        );
    }


    private Payees buildAndSavePendingPayee(Account owner, Account receiver) {

        Payees payees = Payees.builder()
                .ownerAccount(owner)
                .receiverAccount(receiver)
                .verificationStatus(VerificationStatus.PENDING)
                .displayName(receiver.getAccountName())
                .build();

        return payeeRepo.save(payees);
    }

    public void buildAndSaveVerifiedPayee(VopResultEvent vopResultEvent) {

       Payees payee =  payeeRepo.findByCorrelationId(vopResultEvent.correlationId()).
               orElseThrow(()-> new IllegalArgumentException("Account has not begun verification process"));

                payee.setMatchResult(MatchResult.valueOf(vopResultEvent.MatchResult()));
                payee.setVerifiedName(vopResultEvent.actualName()); //Drop verified name from payee,
                payee.setConfidenceScore(vopResultEvent.confidenceScore());
                payee.setVerifiedAt(vopResultEvent.verifiedAt());

        payeeRepo.save(payee);
        mapToPayeeResponse(payee);
    }

    private VopCommandEvent buildVopCommandEvent(String requestedName, String actualName,String correlationId){
        return new VopCommandEvent(requestedName,actualName,correlationId);
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
