package com.bistral.app.bistral_order_service.dtos;

import com.bistral.app.bistral_order_service.entity.enums.PaymentMode;
import com.bistral.app.bistral_order_service.entity.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record PaymentResponse(UUID paymentId, UUID orderId, PaymentStatus status, BigDecimal amount, PaymentMode paymentMode){

}