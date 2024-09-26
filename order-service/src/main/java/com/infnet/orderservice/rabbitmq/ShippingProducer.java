package com.infnet.orderservice.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.orderservice.event.OrderStatusEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingProducer {
    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;

    public void send(OrderStatusEvent orderStatusEvent) throws JsonProcessingException {
        amqpTemplate.convertAndSend("carrier.exc","carrier.rk", objectMapper.writeValueAsString(orderStatusEvent));
    }
}
