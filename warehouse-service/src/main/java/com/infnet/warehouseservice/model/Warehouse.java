package com.infnet.warehouseservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "\"warehouse\"")
public class Warehouse implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "ID do produto é obrigatório")
    private Long productId;

    @NotNull(message = "Quantidade é obrigatório")
    private int quantity;

    @Embedded
    private Localization localization;

    public void sum(int quantity) {
        validate(quantity);
        this.quantity += quantity;
    }

    public void subtract(int quantity) {
        validate(quantity);
        if (this.quantity < quantity) {
            throw new IllegalStateException("Não há estoque suficiente para a remoção.");
        }
        this.quantity -= quantity;
    }

    private void validate(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva.");
        }
    }
}
