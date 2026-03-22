package com.MPS.momer_payments_platform.listener;

import com.MPS.momer_payments_platform.config.RabbitMQConfig;
import com.MPS.momer_payments_platform.events.VopResultEvent;
import com.MPS.momer_payments_platform.service.PayeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class VopResultListener {
    private final PayeeService payeeService;
    private static final Logger log = LoggerFactory.getLogger(VopResultListener.class);

    public VopResultListener(PayeeService payeeService) {
        this.payeeService = payeeService;
    }

    @RabbitListener(queues = RabbitMQConfig.PAYEE_VERIFICATION_RESULT_QUEUE)
    public void handleMatchResult(VopResultEvent vopResultEvent){
        log.info("Vop Match Completed: {}", vopResultEvent.MatchResult());
        log.info("Confidence score: {}", vopResultEvent.confidenceScore());
        payeeService.buildAndSaveVerifiedPayee(vopResultEvent);
    }
}
