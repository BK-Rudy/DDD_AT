package com.infnet.warehouseservice.service.impl;

import com.infnet.warehouseservice.model.OrderItem;
import com.infnet.warehouseservice.model.OrderMessage;
import com.infnet.warehouseservice.model.Warehouse;
import com.infnet.warehouseservice.model.enums.OrderStatus;
import com.infnet.warehouseservice.rabbitmq.PackedProducer;
import com.infnet.warehouseservice.repository.WarehouseRepository;
import com.infnet.warehouseservice.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final PackedProducer packedProducer;

    public void process(OrderMessage orderMessage) {
        log.info("Processando pedido...");

        List<OrderItem> itens = orderMessage.getItems();

        for (OrderItem item : itens) {
            Long productId = item.getProductId();
            int quantity = item.getQuantity();

            Warehouse warehouse = warehouseRepository.findByProductId(productId);
            if (warehouse == null || warehouse.getQuantity() < quantity) {
                log.error("Estoque insuficiente para o produto ID: {}", productId);
                throw new RuntimeException();
            }

            warehouse.subtract(quantity);
            warehouseRepository.save(warehouse);

            log.info("Produto processado com sucesso.");
        }
        processPacked(orderMessage);
    }

    public void processPacked(OrderMessage orderMessage){
        log.info("Processando pedido...");

        try {
            orderMessage.setOrderStatus(OrderStatus.PACKED);

            log.info("Pedido embalado {}",orderMessage);

            packedProducer.send(orderMessage);
        }
        catch (Exception e) {
            log.info("Erro ao embalar o pedido {}, erro: {}",orderMessage, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
