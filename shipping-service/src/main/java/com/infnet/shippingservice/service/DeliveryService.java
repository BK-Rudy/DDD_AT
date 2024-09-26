package com.infnet.shippingservice.service;

import com.infnet.shippingservice.model.OrderMessage;

public interface DeliveryService {
    void process(OrderMessage orderMessage);
    void processDelivered(OrderMessage orderMessage);
}
