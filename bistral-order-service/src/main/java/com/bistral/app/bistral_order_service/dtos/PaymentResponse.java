package com.bistral.app.bistral_order_service.dtos;

import com.bistral.app.bistral_order_service.entity.enums.PaymentStatus;

import java.util.UUID;

public record PaymentResponse(UUID paymentId, UUID orderId, PaymentStatus status){

}