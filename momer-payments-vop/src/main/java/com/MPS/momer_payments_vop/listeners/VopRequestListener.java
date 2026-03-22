package com.MPS.momer_payments_vop.listeners;


import com.MPS.momer_payments_vop.config.RabbitMQConfig;
import com.MPS.momer_payments_vop.events.VopRequestEvent;
import com.MPS.momer_payments_vop.service.VopService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class VopRequestListener {
    private static final Logger log = LoggerFactory.getLogger(VopRequestListener.class);

    private final VopService vopService;

    public VopRequestListener(VopService vopService) {
        this.vopService = vopService;
    }

    @RabbitListener(queues = RabbitMQConfig.VOP_VERIFICATION_REQUESTS_QUEUE)
    public void handleMatchRequest(VopRequestEvent vopRequestEvent) {
        try {
            log.info("Received request for: {}", vopRequestEvent.toString());
            vopService.processMatchRequest(vopRequestEvent);
            log.info("Successfully processed request");
        } catch (Exception e) {
            log.error("Error processing VOP request", e);
            throw e; // Re-throw to let RabbitMQ handle retry
        }
    }
}
