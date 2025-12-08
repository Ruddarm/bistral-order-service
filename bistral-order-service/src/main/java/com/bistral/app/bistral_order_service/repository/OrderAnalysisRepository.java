package com.bistral.app.bistral_order_service.repository;

import com.bistral.app.bistral_order_service.dtos.KpiDTO;
import com.bistral.app.bistral_order_service.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderAnalysisRepository extends JpaRepository<OrderEntity, UUID> {
    @Query(value = """
                SELECT 
                    COALESCE(SUM(o.payable_amount), 0) AS totalRevenue,
                    COALESCE(COUNT(o.order_id), 0) AS totalOrders,
                    COALESCE(AVG(o.payable_amount), 0) AS avgOrderValue
                FROM orders o
                WHERE o.bistro_id IN (:bistroIds)
                  AND o.created_at BETWEEN :startDate AND :endDate
            """, nativeQuery = true)
    KpiDTO getTodayKpi(@Param("bistroIds") List<UUID> bistroIds, @Param("startDate") LocalDateTime start, @Param("endDate") LocalDateTime end);

}
