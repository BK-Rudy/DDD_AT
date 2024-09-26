package com.infnet.orderservice.service;

import com.infnet.orderservice.model.Order;

public interface ShippingService {
    void process(Order order);
}
