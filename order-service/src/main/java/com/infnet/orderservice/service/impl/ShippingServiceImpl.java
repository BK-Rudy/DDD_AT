package com.infnet.orderservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.infnet.orderservice.event.OrderStatusEvent;
import com.infnet.orderservice.model.Order;
import com.infnet.orderservice.rabbitmq.ShippingProducer;
import com.infnet.orderservice.repository.OrderRepository;
import com.infnet.orderservice.service.ShippingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingServiceImpl implements ShippingService {
    private final ShippingProducer shippingProducer;
    private final OrderRepository orderRepository;

    public void process(Order order){
        log.info("Processando pedido...");
        try {
            shippingProducer.send(new OrderStatusEvent(order.getId(), order.getOrderStatus()));
        } catch (JsonProcessingException e) {
            order.error();
        }

        order.inTransit();

        log.info("Pedido processado com sucesso.");

        orderRepository.save(order);
    }
}
