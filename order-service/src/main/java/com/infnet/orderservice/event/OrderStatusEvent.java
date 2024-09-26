package com.infnet.orderservice.event;

import com.infnet.orderservice.model.OrderItemMessage;
import com.infnet.orderservice.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderStatusEvent implements Serializable {

    private Long orderId;
    private OrderStatus orderStatus;
    private Date date;
    private List<OrderItemMessage> orderItems;

    public OrderStatusEvent(Long orderId, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.date = new Date();
    }

    public OrderStatusEvent(Long orderId, OrderStatus orderStatus, Date date) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.date = date;
    }

    public OrderStatusEvent(Long orderId, List<OrderItemMessage> orderItems, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.date = new Date();
    }
}
