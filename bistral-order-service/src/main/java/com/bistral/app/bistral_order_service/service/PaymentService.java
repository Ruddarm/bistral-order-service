package com.bistral.app.bistral_order_service.service;


import com.bistral.app.bistral_order_service.dtos.PaymentRequest;
import com.bistral.app.bistral_order_service.entity.OrderEntity;
import com.bistral.app.bistral_order_service.entity.PaymentEntity;
import com.bistral.app.bistral_order_service.entity.enums.OrderStatus;
import com.bistral.app.bistral_order_service.entity.enums.PaymentStatus;
import com.bistral.app.bistral_order_service.repository.PaymentEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentService {


    private final PaymentEntityRepository paymentRepository;
    private final OrderService orderService;

    @Transactional
    public PaymentEntity makePayment(PaymentRequest paymentRequest) {
        OrderEntity orderEntity = orderService.getOrderByOrderId(paymentRequest.getOrderId());
        PaymentEntity paymentEntity = PaymentEntity
                .builder()
                .order(orderEntity)
                .paymentMode(paymentRequest.getPaymentMode())
                .amount(paymentRequest.getAmount())
                .paymentStatus(PaymentStatus.PAID)
                .build();

        orderEntity.setOrderStatus(OrderStatus.CLOSED);
        orderEntity.setPaymentStatus(PaymentStatus.PAID);
        orderService.saveOrder(orderEntity);
        return  paymentRepository.save(paymentEntity);
    }

}
