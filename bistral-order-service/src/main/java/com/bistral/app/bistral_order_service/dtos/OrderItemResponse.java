package com.bistral.app.bistral_order_service.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private UUID orderItemId;
    private String name;
    private BigDecimal price;
    private BigDecimal taxRate;
    private BigDecimal qty;

}
