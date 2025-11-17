package com.bistral.app.bistral_order_service.controllers;

import com.bistral.app.bistral_order_service.dtos.PaymentResponse;
import com.bistral.app.bistral_order_service.entity.PaymentEntity;
import com.bistral.app.bistral_order_service.entity.enums.PaymentMode;
import com.bistral.app.bistral_order_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/order/{orderId}/mode/{paymentMode}")
    public ResponseEntity<PaymentResponse> makePayment(@PathVariable UUID orderId, @PathVariable PaymentMode paymentMode) {
        PaymentEntity paymentEntity = paymentService.makePayment(paymentMode, orderId);
        return ResponseEntity.ok(new PaymentResponse(paymentEntity.getPaymentId(), paymentEntity.getOrder().getOrderId(), paymentEntity.getPaymentStatus()));
    }
}
