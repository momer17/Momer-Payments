package com.MPS.momer_payments_platform.api.Controller;

import com.MPS.momer_payments_platform.Service.PaymentService;
import com.MPS.momer_payments_platform.api.dto.Payment.CreatePaymentRequest;
import com.MPS.momer_payments_platform.api.dto.Payment.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    private final PaymentService paymentService;

    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse createPaymentResponse(@Valid @RequestBody CreatePaymentRequest paymentRequest){
        return paymentService.createPayment(paymentRequest);
    }


}
