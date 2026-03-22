package com.MPS.momer_payments_vop.publisher;

import com.MPS.momer_payments_vop.config.RabbitMQConfig;
import com.MPS.momer_payments_vop.events.VopResponseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class VopResponsePublisher {
    private final RabbitTemplate rabbitTemplate;
    private static final Logger log = LoggerFactory.getLogger(VopResponsePublisher.class);

    public VopResponsePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishMatchCompleted(VopResponseEvent vopResponseEvent){
        try {

            rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeName,
                    RabbitMQConfig.RK_VERIFICATION_MATCH_RESULT,
                    vopResponseEvent);
            log.info("Vop Match Completed: {}", vopResponseEvent.MatchResult());
            log.info("Confidence score: {}", vopResponseEvent.confidenceScore());
            }catch (Exception e){
                log.error("Error processing VOP request", e);
                throw e; // Re-throw to let RabbitMQ handle retry
            }
    }


}
