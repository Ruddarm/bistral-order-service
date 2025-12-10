package com.bistral.app.bistral_order_service.repository.implementation;

import com.bistral.app.bistral_order_service.dtos.TrendPointDtoImpl;
import com.bistral.app.bistral_order_service.repository.interfaces.IOrderAnalysis;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderAnalysis implements IOrderAnalysis {

    private final EntityManager entityManager;


    @Override
    public List<TrendPointDtoImpl> getOrderTrend(String sql, LocalDateTime start, LocalDateTime end) {
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("start", start);
        query.setParameter("end", end);

        List<Object[]> rows = query.getResultList();

        return rows.stream()
                .map(row -> new TrendPointDtoImpl(
                        (String) row[0],                     // label
                        ((Number) row[1]).doubleValue()      // value
                ))
                .toList();
    }

    @Override
    public List<TrendPointDtoImpl> getRevenueTrend(String sql, LocalDateTime start, LocalDateTime end) {
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("start", start);
        query.setParameter("end", end);

        List<Object[]> rows = query.getResultList();

        return rows.stream()
                .map(row -> new TrendPointDtoImpl(
                        (String) row[0],                     // label
                        ((Number) row[1]).doubleValue()      // value
                ))
                .toList();
    }

    @Override
    public List<TrendPointDtoImpl> getPaymentModeTrend(String sql, LocalDateTime start, LocalDateTime end) {

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("start", start);
        query.setParameter("end", end);

        List<Object[]> rows = query.getResultList();

        return rows.stream()
                .map(row -> new TrendPointDtoImpl(
                        (String) row[0],                     // paymentMode
                        ((Number) row[1]).doubleValue()      // totalAmount
                ))
                .toList();
    }


}
