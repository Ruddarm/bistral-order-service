package com.bistral.app.bistral_order_service.repository.interfaces;

import com.bistral.app.bistral_order_service.dtos.TrendPointDtoImpl;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderAnalysis{
    List<TrendPointDtoImpl> getOrderTrend(String sql, LocalDateTime start, LocalDateTime end);
    List<TrendPointDtoImpl> getRevenueTrend(String sql, LocalDateTime start, LocalDateTime end);
    List<TrendPointDtoImpl> getPaymentModeTrend(String sql, LocalDateTime start, LocalDateTime end);
}
