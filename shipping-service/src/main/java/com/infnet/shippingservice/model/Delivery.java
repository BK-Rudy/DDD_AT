package com.infnet.shippingservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "\"delivery\"")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "ID do pedido é obrigatório")
    @Column(nullable = false)
    private Long orderId;

    @NotNull(message = "Frete é obrigatório")
    @Column(nullable = false)
    private Float freight;

    @NotNull(message = "ID do seguro é obrigatório")
    @Column(name = "insurance_id", nullable = false)
    private Long insuranceId;

    @NotNull(message = "ID do motorista é obrigatório")
    @Column(name = "driver_id",nullable = false)
    private Long driverId;

    @Embedded
    private Address address;
}
