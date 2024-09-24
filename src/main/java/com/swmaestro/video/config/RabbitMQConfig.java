package com.swmaestro.video.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
public class RabbitMQConfig {
    private static final String HOST_SERVER = "54.180.140.202";

    @Bean
    ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(HOST_SERVER);
        return connectionFactory;
    }

    @Bean
    Connection connection() throws IOException, TimeoutException {
        return connectionFactory().newConnection();
    }
}
