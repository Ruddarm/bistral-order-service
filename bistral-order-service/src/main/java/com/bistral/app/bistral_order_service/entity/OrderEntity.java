package com.bistral.app.bistral_order_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @OneToMany(mappedBy = "order")
    private List<OrderItemEntity> orderItemList = new ArrayList<>();
}
