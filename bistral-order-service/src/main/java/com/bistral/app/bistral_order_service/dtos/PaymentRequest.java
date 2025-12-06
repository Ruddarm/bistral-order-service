package com.bistral.app.bistral_order_service.dtos;

import com.bistral.app.bistral_order_service.entity.enums.PaymentMode;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    UUID orderId;
    PaymentMode paymentMode;
    BigDecimal amount;
}
