package com.MPS.momer_payments_platform.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;


@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String topicExchangeName = "momer.exchange";
    //TODO: Connect to orchestrator queue after
    //public static final String PAYEEE_VERIFICATION_COMMAND_QUEUE = "payee.verification-command";
    public static final String PAYEEE_VERIFICATION_COMMAND_QUEUE = "vop.verification-requests";
    public static final String PAYEE_VERIFICATION_RESULT_QUEUE = "vop.verification-result";



    public static final String RK_VERIFICATION_EXECUTE = "vop.match.requested";
    public static final String RK_VERIFICATION_MATCH_RESULT = "vop.match.result";


    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }


    @Bean
    Queue payeeVerificationCommandQueue(){
        return new Queue(PAYEEE_VERIFICATION_COMMAND_QUEUE,true);
    }

    @Bean
    Queue payeeVerificationResultsQueue(){
        return new Queue(PAYEE_VERIFICATION_RESULT_QUEUE,true);
    }



    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding payeeVerificationCommandBinding(TopicExchange exchange){
        return BindingBuilder.bind(payeeVerificationCommandQueue()).to(exchange).with(RK_VERIFICATION_EXECUTE);
    }

    @Bean
    Binding payeeVerificationResultsBinding(TopicExchange exchange){
        return BindingBuilder.bind(payeeVerificationResultsQueue()).to(exchange).with(RK_VERIFICATION_MATCH_RESULT);
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public ConnectionFactory connectionFactory(
            @Value("${spring.rabbitmq.host:localhost}") String host,
            @Value("${spring.rabbitmq.port:5672}") int port,
            @Value("${spring.rabbitmq.username:guest}") String username,
            @Value("${spring.rabbitmq.password:guest}") String password) {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    /*
    Orcherstrator listens for payment event from payment service, then routes the request to payee service,
    listens for update then just publishes the result only from the service back to the payee
     */



}
