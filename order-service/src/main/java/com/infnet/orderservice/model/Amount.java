package com.infnet.orderservice.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Amount implements Serializable {
    private final BigDecimal amount;

    public Amount(BigDecimal amount) {
        validate(amount);
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Amount sum(Amount amount2) {
        validate(amount2);
        return new Amount(this.amount.add(amount2.getAmount()));
    }

    @Override
    public boolean equals(Object object) {
        final Amount amount2 = (Amount) object;
        return Objects.equals(this.amount, amount2.getAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    private void validate(Amount amount2) {
        if (amount2 == null || amount2.amount.signum() < 0) {
            throw new IllegalArgumentException("Montante não pode ser negativo ou nulo");
        }
    }

    private void validate(BigDecimal amount) {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("Montante não pode ser negativo");
        }
    }
}
