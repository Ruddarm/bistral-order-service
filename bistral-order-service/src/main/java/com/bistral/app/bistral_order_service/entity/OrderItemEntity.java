package com.bistral.app.bistral_order_service.entity;

import com.bistral.app.bistral_order_service.entity.enums.ItemUnit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "OrderItem", indexes = {@Index(name = "orderId", columnList = "orderId")})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID orderItemId;
    private String name;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxRate;
    @Column(nullable = false)
    private BigDecimal qty;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemUnit unit;
    @ManyToOne()
    @JoinColumn(name = "orderId", nullable = false)
    private OrderEntity order;
    @Column(nullable = false)
    private UUID menuItemId;

    public BigDecimal getTaxableAmount() {
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalPrice() {
        return this.price.multiply(this.qty);
    }
}
