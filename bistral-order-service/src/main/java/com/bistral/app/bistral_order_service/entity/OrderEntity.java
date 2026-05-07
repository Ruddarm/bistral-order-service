package com.bistral.app.bistral_order_service.entity;

import com.bistral.app.bistral_order_service.enums.OrderStatus;
import com.bistral.app.bistral_order_service.enums.OrderType;
import com.bistral.app.bistral_order_service.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "Orders",
        indexes = {
                @Index(name = "orderId_BistroId_",
                        columnList = "bistroId, branchId"
                )
        }
)
@BatchSize(size = 50)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;
    @Column(nullable = false)
    private UUID bistroId;
    @Column()
    private UUID tableId;
    @Column(nullable = false)
    private UUID branchId;

    private int tableNo;

    //    @Column(name = )
//    private long orderNumber;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discount;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxableAmount;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal payableAmount;


    private OrderType orderType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.OPEN;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.ALL})
    private Set<PaymentEntity> paymentEntities = new HashSet<>();
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItemEntityList = new ArrayList<>();
//
//    @Column(nullable = false,updatable = false)
//    @CreationTimestamp
//    private LocalDateTime createdAt;
//
//    @Column(nullable = false,updatable = false)
//    private UUID createdBy;
//
//    @Column
//    private UUID updatedBy;
//
//    @Column
//    @UpdateTimestamp
//    private LocalDateTime updatedAt;

    public void reCalcTotals() {
        this.taxableAmount = orderItemEntityList.stream()
                .map(OrderItemEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.taxAmount = orderItemEntityList.stream()
                .map(OrderItemEntity::getTaxableAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.payableAmount = this.taxableAmount.subtract(this.taxAmount);
    }
}
