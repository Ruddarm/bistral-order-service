package com.bistral.app.bistral_order_service.dtos;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrderItemResponse {

    private UUID itemId;
    private String name;
    private BigDecimal price;
    private BigDecimal taxRate;
    private BigDecimal qty;

}
