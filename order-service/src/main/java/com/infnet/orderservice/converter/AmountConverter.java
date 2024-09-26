package com.infnet.orderservice.converter;

import com.infnet.orderservice.model.Amount;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

@Converter(autoApply = true)
public class AmountConverter implements AttributeConverter<Amount, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Amount amount) {
        return amount.getAmount();
    }

    @Override
    public Amount convertToEntityAttribute(BigDecimal amount) {
        return new Amount(amount);
    }
}
