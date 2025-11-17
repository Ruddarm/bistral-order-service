package com.bistral.app.bistral_order_service.repository;

import com.bistral.app.bistral_order_service.entity.OrderEntity;
import com.bistral.app.bistral_order_service.entity.enums.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @Query("select o from OrderEntity o left join fetch o.orderItemEntityList where o.orderId = :orderId")
    Optional<OrderEntity> findByOrderId(@Param("orderId") UUID orderId);

    @Transactional
    @Modifying
    @Query("UPDATE OrderEntity o SET o.taxableAmount = :taxableAmount, o.taxAmount = :taxAmount, o.payableAmount  = :payableAmount WHERE o.orderId = :orderId")
    void updateTotals(UUID orderId, BigDecimal taxableAmount, BigDecimal taxAmount, BigDecimal payableAmount);

    @Query("""
                select distinct o 
                from OrderEntity o 
                left join fetch o.orderItemEntityList 
                where o.branchId = :branchId 
                  and o.orderStatus = :status
            """)
    List<OrderEntity> findByBranchIdAndOrderStatus(UUID branchId, OrderStatus status);
}
