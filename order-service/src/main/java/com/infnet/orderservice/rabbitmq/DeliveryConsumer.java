package com.infnet.orderservice.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.orderservice.event.OrderStatusEvent;
import com.infnet.orderservice.model.Order;
import com.infnet.orderservice.repository.OrderRepository;
import com.infnet.orderservice.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryConsumer {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @RabbitListener(queues = {"delivery"})
    public void receive(@Payload String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderStatusEvent orderMessage = objectMapper.readValue(message, OrderStatusEvent.class);

        try {
            Optional<Order> optionalOrder = orderService.findById(orderMessage.getOrderId());
            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                order.setOrderStatus(orderMessage.getOrderStatus());
                orderRepository.save(order);

                channel.basicAck(deliveryTag, false);
                log.info("Mensagem recebida: {}", orderMessage);
            } else {
                log.error("Pedido não encontrado: {}", orderMessage.getOrderId());
                channel.basicNack(deliveryTag, false, false);
            }
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);
            log.error("Erro ao receber mensagem: {}, erro: {}", message, e.getMessage());
        }
    }
}
