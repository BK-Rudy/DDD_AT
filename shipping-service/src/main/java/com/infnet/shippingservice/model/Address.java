package com.infnet.shippingservice.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class Address {
    private String zipCode;

    private String streetName;

    private String number;

    private String complement;

    private String neighborhood;

    private String city;

    private String state;
}
