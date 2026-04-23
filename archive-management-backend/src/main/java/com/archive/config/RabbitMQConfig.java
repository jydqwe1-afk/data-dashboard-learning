package com.archive.config;

import com.archive.common.Constants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public DirectExchange logExchange() {
        return new DirectExchange(Constants.MQ_LOG_EXCHANGE);
    }

    @Bean
    public Queue logQueue() {
        return QueueBuilder.durable(Constants.MQ_LOG_QUEUE).build();
    }

    @Bean
    public Binding logBinding(Queue logQueue, DirectExchange logExchange) {
        return BindingBuilder.bind(logQueue).to(logExchange).with(Constants.MQ_LOG_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
}
