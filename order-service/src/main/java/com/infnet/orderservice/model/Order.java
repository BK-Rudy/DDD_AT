package com.infnet.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infnet.orderservice.converter.AmountConverter;
import com.infnet.orderservice.converter.OrderStatusConverter;
import com.infnet.orderservice.model.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
@Builder
@Table(name = "\"order\"")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "order_date")
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @JsonIgnoreProperties(value = "orderId")
    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @NotNull(message = "ID do cliente é obrigatório")
    @Column(name = "customer_id")
    private Long customerId;

    @Convert(converter = OrderStatusConverter.class)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Convert(converter = AmountConverter.class)
    @Column(name = "total_value")
    private Amount totalValue;

    public Order() {
        this.orderDate = new Date();
        this.orderStatus = OrderStatus.CREATED;
        this.totalValue = new Amount(BigDecimal.ZERO);
    }

    public void addItem(Long productId, int quantity, double unitValue) {
        validate(productId, quantity);
        validateOrderStatus("inserir itens", OrderStatus.CREATED);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(this);
        orderItem.setProductId(productId);
        orderItem.setQuantity(quantity);
        orderItem.setTotal(new Amount(new BigDecimal(unitValue * quantity)));

        if (this.orderItems == null) {
            this.orderItems = new ArrayList<>();
        }

        this.orderItems.add(orderItem);
        this.setTotalValue(this.totalValue.sum(orderItem.getTotal()));
    }

    public void close() {
        validateOrderStatus("fechar", OrderStatus.CREATED);
        if (this.orderItems.isEmpty()) {
            throw new IllegalStateException("Não é possível fechar um pedido vazio");
        }
        this.orderStatus = OrderStatus.CLOSED;
    }

    public void cancel() {
        validateOrderStatus("cancelar", OrderStatus.CLOSED);
        this.orderStatus = OrderStatus.CANCELLED;
    }

    public void error() {
        this.orderStatus = OrderStatus.ERROR;
    }

    public void send() {
        validateOrderStatus("enviar", OrderStatus.CLOSED);
        this.orderStatus = OrderStatus.IN_PREPARATION;
    }

    public void inTransit() {
        validateOrderStatus("embalar", OrderStatus.PACKED);
        this.orderStatus = OrderStatus.IN_TRANSIT;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Order)) {
            return false;
        }
        Order other = (Order) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    private void validate(Long productId, int quantity) {
        if (productId == null) {
            throw new IllegalArgumentException("Produto inválido");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("A quantidade do produto deve ser 1 no mínimo");
        }
    }

    private void validateOrderStatus(String action, OrderStatus expectedStatus) {
        if (this.orderStatus != expectedStatus) {
            throw new IllegalStateException("Não é possível " + action + " um pedido que não esteja no status " + expectedStatus);
        }
    }
}
