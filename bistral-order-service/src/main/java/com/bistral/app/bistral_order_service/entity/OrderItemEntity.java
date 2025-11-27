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
    //store items name
    private String name;
    //item price
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    // item tax rate
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxRate;
    // item ordered qty
    @Column(nullable = false)
    private BigDecimal orderedQty;
    // item unit
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemUnit unit;
    //order id
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinColumn(name = "orderId", nullable = false)
    private OrderEntity order;
    @Column(nullable = false)
    private UUID menuItemId;



    public BigDecimal getTaxableAmount() {
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalPrice() {
        return this.price.multiply(this.orderedQty);
    }
}
