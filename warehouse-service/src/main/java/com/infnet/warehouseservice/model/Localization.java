package com.infnet.warehouseservice.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Localization {
    private String zone;
    private String section;
    private String floor;
    private String aisle;
    private String shelf;
    private String box;
}
