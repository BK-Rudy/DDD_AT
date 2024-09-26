package com.infnet.shippingservice.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.shippingservice.model.OrderMessage;
import com.infnet.shippingservice.service.DeliveryService;
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
public class CarrierConsumer {
    private final DeliveryService deliveryService;

    @RabbitListener(queues = {"carrier"})
    public void receive(@Payload String mensagem, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderMessage orderMessage = objectMapper.readValue(mensagem, OrderMessage.class);

        try {
            log.info("Recebendo pedido: {}", orderMessage);

            deliveryService.process(orderMessage);
            channel.basicAck(deliveryTag, false);

            log.info("Mensagem recebida: {}", orderMessage);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);

            log.error("Erro ao receber mensagem: {}, erro: {}", mensagem, e.getMessage());
        }
    }
}
