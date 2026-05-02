package com.bistral.app.bistral_order_service.entity;


import com.bistral.app.bistral_order_service.enums.PaymentMode;
import com.bistral.app.bistral_order_service.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID paymentId;
//    @JoinColumn(name = "orderId")
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "orderId")
    OrderEntity order;
    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    PaymentMode paymentMode;
    @CurrentTimestamp
    LocalDateTime paidAt;
    BigDecimal amount=BigDecimal.ZERO;

}
