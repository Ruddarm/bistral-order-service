package com.bistral.app.bistral_order_service.dtos;


import com.bistral.app.bistral_order_service.enums.OrderStatus;
import com.bistral.app.bistral_order_service.enums.PaymentStatus;
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
    private UUID tableId;
    private UUID branchId;
    private int tableNo;
    private OrderStatus orderStatus;
    private BigDecimal taxAmount;
    private BigDecimal discount;
    private BigDecimal taxableAmount;
    private BigDecimal payableAmount;
    private String orderType;
    private List<OrderItemResponse> orderItemList = new ArrayList<>();
    private List<PaymentResponse> paymentResponseList = new ArrayList<>();
    private PaymentStatus paymentStatus;
}
