package com.bistral.app.bistral_order_service.repository;

import com.bistral.app.bistral_order_service.dtos.KpiDTO;
import com.bistral.app.bistral_order_service.dtos.RecentOrderDto;
import com.bistral.app.bistral_order_service.dtos.TrendPointDto;
import com.bistral.app.bistral_order_service.dtos.TrendPointDtoImpl;
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

    @Query(value = """
                SELECT 
                    o.order_number AS orderNumber,
                    COUNT(oi.order_id) AS itemCount,
                    o.table_no AS tableNumber,
                    o.payable_amount AS payableAmount
                FROM orders o
                LEFT JOIN order_item oi ON oi.order_id = o.order_id
                WHERE o.bistro_id IN (:bistroIds)
                GROUP BY 
                    o.order_id
                ORDER BY o.created_at DESC
                LIMIT 4
            """, nativeQuery = true)
    List<RecentOrderDto> getRecentOrders(@Param("bistroIds") List<UUID> bistroIds);

    @Query(
            value = """
                                SELECT
                                    p.payment_mode AS label,
                                    COALESCE(SUM(p.amount), 0) AS value
                                FROM payment_entity p
                                JOIN orders o ON o.order_id = p.order_id
                                WHERE p.paid_at BETWEEN :start AND :end
                                  AND o.bistro_id IN (:bistroIds)
                                GROUP BY p.payment_mode
                    
                    """
            ,nativeQuery = true
    )
    List<TrendPointDto> getPaymentModeDistribution(@Param("bistroIds") List<UUID> bistroIds,
                                                   LocalDateTime start,
                                                   LocalDateTime end
    );
//    @Query(value = """
//                SELECT
//                    TO_CHAR(date_bucket, format_pattern) AS label,
//                    COUNT(*) AS value
//                FROM (
//                    SELECT
//                        date_trunc('hour', o.created_at) AS hour_bucket,
//                        date_trunc('day', o.created_at) AS day_bucket,
//                        date_trunc('week', o.created_at) AS week_bucket,
//                        date_trunc('month', o.created_at) AS month_bucket,
//
//                        CASE
//                            WHEN :groupBy = 'hour' THEN date_trunc('hour', o.created_at)
//                            WHEN :groupBy = 'day' THEN date_trunc('day', o.created_at)
//                            WHEN :groupBy = 'week' THEN date_trunc('week', o.created_at)
//                            WHEN :groupBy = 'month' THEN date_trunc('month', o.created_at)
//                        END AS date_bucket,
//
//                        CASE
//                            WHEN :groupBy = 'hour' THEN 'HH24'
//                            WHEN :groupBy = 'day' THEN 'DD Mon'
//                            WHEN :groupBy = 'week' THEN '"Week "IW'
//                            WHEN :groupBy = 'month' THEN 'Mon YYYY'
//                        END AS format_pattern
//                    FROM orders o
//                    WHERE o.bistro_id IN (:bistroIds)
//                      AND o.created_at BETWEEN :start AND :end
//                ) t
//                GROUP BY date_bucket, format_pattern
//                ORDER BY date_bucket
//            """, nativeQuery = true)
//    List<TrendPointDtoImpl> getOrderTrend(
//            @Param("bistroIds") List<UUID> bistroIds,
//            @Param("branchIds") List<UUID> branchIds,
//            @Param("groupBy") String groupBy,
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end
//    );
//
//    @Query(value = """
//                SELECT
//                    TO_CHAR(date_bucket, format_pattern) AS label,
//                    COALESCE(SUM(o.payable_amount),0)::double precision AS value
//                FROM (
//                    SELECT
//                        date_trunc('hour', o.created_at) AS hour_bucket,
//                        date_trunc('day', o.created_at) AS day_bucket,
//                        date_trunc('week', o.created_at) AS week_bucket,
//                        date_trunc('month', o.created_at) AS month_bucket,
//
//                        CASE
//                            WHEN :groupBy = 'hour' THEN date_trunc('hour', o.created_at)
//                            WHEN :groupBy = 'day' THEN date_trunc('day', o.created_at)
//                            WHEN :groupBy = 'week' THEN date_trunc('week', o.created_at)
//                            WHEN :groupBy = 'month' THEN date_trunc('month', o.created_at)
//                        END AS date_bucket,
//
//                        CASE
//                            WHEN :groupBy = 'hour' THEN 'HH24'
//                            WHEN :groupBy = 'day' THEN 'DD Mon'
//                            WHEN :groupBy = 'week' THEN '"Week "IW'
//                            WHEN :groupBy = 'month' THEN 'Mon YYYY'
//                        END AS format_pattern
//                    FROM orders o
//                    WHERE o.bistro_id IN (:bistroIds)
//                      AND o.created_at BETWEEN :start AND :end
//                ) t
//                GROUP BY date_bucket, format_pattern
//                ORDER BY date_bucket
//            """, nativeQuery = true)
//    List<TrendPointDtoImpl> getRevenueTrend(
//            @Param("bistroIds") List<UUID> bistroIds,
//            @Param("branchIds") List<UUID> branchIds,
//            @Param("groupBy") String groupBy,
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end
//    );


}
