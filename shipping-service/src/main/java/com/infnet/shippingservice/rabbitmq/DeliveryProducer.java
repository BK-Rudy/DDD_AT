package com.infnet.shippingservice.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.shippingservice.model.OrderMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryProducer {
    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;

    public void send(OrderMessage orderMessage) throws JsonProcessingException {
        amqpTemplate.convertAndSend("delivery.exc","delivery.rk", objectMapper.writeValueAsString(orderMessage));
    }
}
