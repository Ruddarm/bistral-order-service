package com.bistral.app.bistral_order_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "OrderItem",
        indexes = {
                @Index(name = "orderId"
                        ,
                        columnList = "orderId"
                )
        }
)
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID itemId;
    private String name;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxRate;
    @Column(nullable = false)
    private BigDecimal qty;
    @ManyToOne()
    @JoinColumn(name = "orderId", nullable = false)
    private OrderEntity order;

}
