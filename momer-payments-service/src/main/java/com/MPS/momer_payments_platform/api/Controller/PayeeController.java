package com.MPS.momer_payments_platform.api.Controller;

import com.MPS.momer_payments_platform.Domain.Payees;
import com.MPS.momer_payments_platform.Service.PayeeService;
import com.MPS.momer_payments_platform.api.dto.Payees.CreatePayeeRequest;
import com.MPS.momer_payments_platform.api.dto.Payees.DeletePayeeRequest;
import com.MPS.momer_payments_platform.api.dto.Payees.PayeeResponse;
import com.MPS.momer_payments_platform.api.dto.Payees.UpdatePayeeRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payee")
public class PayeeController {
    private final PayeeService payeeService;

    public PayeeController(PayeeService payeeService) {
        this.payeeService = payeeService;
    }

    @PostMapping
    public PayeeResponse createPayee(@Valid @RequestBody CreatePayeeRequest payeeRequest){
        return payeeService.createPayee(payeeRequest);
    }

    @PutMapping
    public PayeeResponse updatePayeeDisplayName(@Valid @RequestBody UpdatePayeeRequest updatePayeeRequest){
        return payeeService.updatePayeeDisplayName(updatePayeeRequest);
    }

    @DeleteMapping
    public void deletePayee(@Valid @RequestBody DeletePayeeRequest deletePayeeRequest){
        payeeService.deletePayee(deletePayeeRequest);
    }

    @GetMapping
    public List<Payees> getPayeesByAccount (@Valid @RequestParam(value = "ownerAccountUUID") UUID ownerAccountID){
        return payeeService.getAllPayeesByOwnerAccountId(ownerAccountID);
    }





}
