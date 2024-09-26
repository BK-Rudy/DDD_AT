package com.infnet.warehouseservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.infnet.warehouseservice.model.OrderMessage;
import com.infnet.warehouseservice.model.enums.OrderStatus;

public interface WarehouseService {
    void process(OrderMessage orderMessage);
    void processPacked(OrderMessage orderMessage);
}
