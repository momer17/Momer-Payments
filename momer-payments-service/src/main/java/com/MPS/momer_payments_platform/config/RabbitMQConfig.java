package com.MPS.momer_payments_platform.config;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Configuration;

import javax.sound.midi.Receiver;

@Configuration
public class RabbitMQConfig {


    //TODO: Implement Payee RabbitMQ service

    static final String topicExchangeName = "payment-exchange";
    static final String VOP_VERIFICATION_REQUESTS_QUEUE = ".verification-requests";
    static final String VOP_VERIFICATION_COMPLETED_QUEUE = "vop.verification-match-completed";


    @Bean
    Queue vopVerificationRequestedQueue(){
        return new Queue(VOP_VERIFICATION_REQUESTS_QUEUE,true);
    }
    @Bean
    Queue vopVerificationCompletedQueue(){
        return new Queue(VOP_VERIFICATION_COMPLETED_QUEUE,true);
    }

    @Bean
    TopicExchange exchange(){
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding vopVerificationRequestedBinding(TopicExchange exchange){
        return BindingBuilder.bind(vopVerificationRequestedQueue()).to(exchange).with("payment-exchange");
    }
    @Bean
    Binding vopVerificationCompletedBinding(TopicExchange exchange){
        return BindingBuilder.bind(vopVerificationCompletedQueue()).to(exchange).with("payment-exchange");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(VOP_VERIFICATION_REQUESTS_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new JacksonJsonMessageConverter();
    }







}
