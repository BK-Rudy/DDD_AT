package com.infnet.orderservice.service;

import com.infnet.orderservice.model.Order;

import java.util.Optional;

public interface OrderService {
    Optional<Order> findById(Long id) throws Exception;

    Order close(long id);
}
