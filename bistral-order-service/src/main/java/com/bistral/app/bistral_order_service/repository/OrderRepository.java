package com.bistral.app.bistral_order_service.repository;

import com.bistral.app.bistral_order_service.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @Query("select o from OrderEntity o left join fetch o.orderItemMap where o.orderId = :orderId")
    public Optional<OrderEntity> findByOrderId(@Param("orderId") UUID orderId);
    @Modifying
    @Query("UPDATE OrderEntity o SET o.totalAmount = :total, o.taxableAmount = :taxable, o.payableAmount = :payable WHERE o.orderId = :orderId")
    void updateTotals(UUID orderId, BigDecimal total, BigDecimal taxable, BigDecimal payable);

}
