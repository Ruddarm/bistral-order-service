package com.bistral.app.bistral_order_service.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class OrderResponse {
    private UUID orderId;
    private UUID bistroId;
    private UUID branchId;
    private int tableNo;
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal taxableAmount;
    private BigDecimal payableAmount;
    private String orderType;
    private List<?> orderItemList = new ArrayList<>();
}
