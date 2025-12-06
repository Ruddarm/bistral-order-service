package com.bistral.app.bistral_order_service.controllers;

import com.bistral.app.bistral_order_service.dtos.PaymentRequest;
import com.bistral.app.bistral_order_service.dtos.PaymentResponse;
import com.bistral.app.bistral_order_service.entity.PaymentEntity;
import com.bistral.app.bistral_order_service.entity.enums.PaymentMode;
import com.bistral.app.bistral_order_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/order")
    public ResponseEntity<PaymentResponse> makePayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentEntity paymentEntity = paymentService.makePayment(paymentRequest);
        return ResponseEntity.ok(new PaymentResponse(paymentEntity.getPaymentId(), paymentEntity.getOrder().getOrderId(), paymentEntity.getPaymentStatus(), paymentEntity.getAmount(),paymentEntity.getPaymentMode()));
    }
}
