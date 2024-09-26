package com.infnet.orderservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.infnet.orderservice.event.OrderStatusEvent;
import com.infnet.orderservice.model.Order;
import com.infnet.orderservice.model.OrderItemMessage;
import com.infnet.orderservice.rabbitmq.WarehouseProducer;
import com.infnet.orderservice.repository.OrderRepository;
import com.infnet.orderservice.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseProducer warehouseProducer;
    private final OrderRepository orderRepository;

    public void process(Order order){
        log.info("Processando pedido...");

        try {
            List<OrderItemMessage> itens = order.getOrderItems().stream()
                    .map(item -> new OrderItemMessage(item.getQuantity(), item.getProductId()))
                    .toList();
            warehouseProducer.send(new OrderStatusEvent(order.getId(),itens,order.getOrderStatus()));
        } catch (JsonProcessingException e) {
            order.error();
        }

        order.send();

        log.info("Pedido processado com sucesso.");

        orderRepository.save(order);
    }
}
