package com.infnet.orderservice.converter;

import com.infnet.orderservice.model.enums.OrderStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus orderStatus) {
        return orderStatus.toString();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String orderStatus) {
        return OrderStatus.valueOf(orderStatus);
    }
}
