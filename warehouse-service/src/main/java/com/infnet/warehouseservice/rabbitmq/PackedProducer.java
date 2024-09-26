package com.infnet.warehouseservice.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.warehouseservice.model.OrderMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PackedProducer {
    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;

    public void send(OrderMessage mensagem) throws JsonProcessingException {
        amqpTemplate.convertAndSend("packed.exc","packed.rk", objectMapper.writeValueAsString(mensagem));
    }
}
