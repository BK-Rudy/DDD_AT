package com.infnet.orderservice.service.impl;

import com.infnet.orderservice.model.Order;
import com.infnet.orderservice.repository.OrderRepository;
import com.infnet.orderservice.service.OrderService;
import com.infnet.orderservice.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final WarehouseService warehouseService;

    @Override
    public Optional<Order> findById(Long id) throws Exception {
        log.info("Buscando pedido...");
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new Exception("Erro: Pedido com ID: " + id + " não encontrado."));

        log.info("Pedido encontrado com sucesso.");

        return Optional.of(existingOrder);
    }

    public Order close(long id) {
        log.info("Fechando pedido...");

        Order order = orderRepository.getReferenceById(id);
        order.close();
        order = orderRepository.save(order);
        warehouseService.process(order);

        log.info("Pedido fechado com sucesso.");
        return order;
    }
}
