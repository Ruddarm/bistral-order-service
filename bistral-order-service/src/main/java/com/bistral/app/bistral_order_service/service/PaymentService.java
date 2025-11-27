package com.bistral.app.bistral_order_service.service;


import com.bistral.app.bistral_order_service.entity.OrderEntity;
import com.bistral.app.bistral_order_service.entity.PaymentEntity;
import com.bistral.app.bistral_order_service.entity.enums.OrderStatus;
import com.bistral.app.bistral_order_service.entity.enums.PaymentMode;
import com.bistral.app.bistral_order_service.entity.enums.PaymentStatus;
import com.bistral.app.bistral_order_service.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PaymentService {


    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    @Transactional
    public PaymentEntity makePayment(PaymentMode paymentMode, UUID orderId) {
        OrderEntity orderEntity = orderService.getOrderByOrderId(orderId);
        PaymentEntity paymentEntity = PaymentEntity
                .builder()
                .order(orderEntity)
                .paymentMode(paymentMode)
                .paymentStatus(PaymentStatus.PAID)
                .build();
        orderEntity.setOrderStatus(OrderStatus.CLOSED);
        orderEntity.setPaymentStatus(PaymentStatus.PAID);
        orderService.saveOrder(orderEntity);
        return  paymentRepository.save(paymentEntity);
    }

}
