package com.MPS.momer_payments_platform.publisher;

import com.MPS.momer_payments_platform.config.RabbitMQConfig;
import com.MPS.momer_payments_platform.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class VopCommandPublisher {
    public final RabbitTemplate rabbitTemplate;
    private static final Logger log = LoggerFactory.getLogger(VopCommandPublisher.class);

    public VopCommandPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishVopExecuteCommand(VopCommandEvent vopCommandEvent){
        rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeName,
                RabbitMQConfig.RK_VERIFICATION_EXECUTE,
                vopCommandEvent);
        log.info("Vop Match Requested: {}",vopCommandEvent.toString());
    }

}
