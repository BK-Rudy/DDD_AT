package com.infnet.orderservice.service;

import com.infnet.orderservice.model.Order;

public interface WarehouseService {
    void process(Order order);
}
