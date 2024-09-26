package com.infnet.warehouseservice.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.warehouseservice.model.OrderMessage;
import com.infnet.warehouseservice.service.WarehouseService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseConsumer {
    private final WarehouseService warehouseService;

    @RabbitListener(queues = {"warehouse"})
    public void receive(@Payload String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderMessage orderMessage = objectMapper.readValue(message, OrderMessage.class);

        try {
            warehouseService.process(orderMessage);
            channel.basicAck(deliveryTag, false);

            log.info("Mensagem recebida: {}", orderMessage);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);
            log.error("Erro ao receber mensagem: {}, erro: {}", message, e.getMessage());
        }
    }
}
