package com.MPS.momer_payments_orchestrator.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String topicExchangeName = "momer.exchange";
    public static final String ORCH_PAYEE_VERIFICATION_REQUESTS_QUEUE = "orchestrator.payee-verification-requests";
    public static final String ORCH_VERIFICATION_COMPLETED_QUEUE = "orchestrator.verification-completed";

    public static final String RK_PAYEE_VERIFICATION_REQUESTED = "payee.verification.requested";
    public static final String RK_PAYEE_VERIFICATION_RESULTED = "payee.verification.resulted";
    public static final String RK_VERIFICATION_REQUESTED = "verification.requested";
    public static final String RK_VERIFICATION_COMPLETED = "verification.completed";


    @Bean
    public Queue orchPayeeVerificationRequestedQueue(){
        return new Queue(ORCH_PAYEE_VERIFICATION_REQUESTS_QUEUE,true);
    }
    @Bean
    public Queue orchVerificationCompletedQueue(){
        return new Queue(ORCH_VERIFICATION_COMPLETED_QUEUE,true);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    public Binding orchPayeeVerificationRequestsBinding(Queue orchPayeeVerificationRequestedQueue,TopicExchange exchange){
        return BindingBuilder.bind(orchPayeeVerificationRequestedQueue).to(exchange).with(RK_PAYEE_VERIFICATION_REQUESTED);
    }
    @Bean
    public Binding orchVerifiedCompletedBinding(Queue orchVerificationCompletedQueue, TopicExchange exchange){
        return BindingBuilder.bind(orchVerificationCompletedQueue).to(exchange).with(RK_VERIFICATION_COMPLETED);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new JacksonJsonMessageConverter();
    }


}
