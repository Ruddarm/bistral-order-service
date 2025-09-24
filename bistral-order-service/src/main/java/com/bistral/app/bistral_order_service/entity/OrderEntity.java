package com.bistral.app.bistral_order_service.entity;


import com.bistral.app.bistral_order_service.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;
    @Column(nullable = false)
    private UUID bistroId;
    @Column(nullable = false)
    private UUID tableId;
    @Column(nullable = false)
    private UUID branchId;
    private int tableNo;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discount;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxableAmount;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal payableAmount;
    private String orderType;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus=OrderStatus.Open;
    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @MapKey(name = "orderItemId")
    private Map<UUID, OrderItemEntity> orderItemMap = new HashMap<>();

    public void reCalcTotals() {
        this.totalAmount = orderItemMap.values().stream()
                .map(OrderItemEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.taxableAmount = orderItemMap.values().stream()
                .map(OrderItemEntity::getTaxableAmount)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        this.payableAmount=this.payableAmount.subtract(this.taxableAmount);
    }


}
