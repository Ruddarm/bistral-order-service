package com.bistral.app.bistral_order_service.controllers;


import com.bistral.app.bistral_order_service.dtos.KpiDTO;
import com.bistral.app.bistral_order_service.dtos.RecentOrderDto;
import com.bistral.app.bistral_order_service.dtos.TrendPointDto;
import com.bistral.app.bistral_order_service.dtos.TrendPointDtoImpl;
import com.bistral.app.bistral_order_service.service.OrderAnalysisService;
import com.bistral.app.bistral_order_service.service.implementation.TrendService;
import com.bistral.app.bistral_order_service.utils.Range;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final OrderAnalysisService orderAnalysisService;

    //    private  final OrderAnalysisService
    private final TrendService trendService;

    @GetMapping("/kpi")
    public ResponseEntity<KpiDTO> getKpi(
            @RequestParam Range range,
            @RequestParam List<UUID> bistroIds,
            @RequestParam(required = false) List<UUID> branchIds
    ) {
        return ResponseEntity.ok(orderAnalysisService.getKpis(bistroIds, branchIds, range));
    }

    @GetMapping("/recentOrder")
    public ResponseEntity<List<RecentOrderDto>> getRecentOrder(
            @RequestParam List<UUID> bistroIds
    ) {
        return ResponseEntity.ok(orderAnalysisService.getRecentOrders(bistroIds));
    }

    @GetMapping("/trend/order")
    public ResponseEntity<List<TrendPointDto>> getOrderTrend(
            @RequestParam List<UUID> bistroIds,
            @RequestParam(required = false) List<UUID> branchIds,
            Range range
    ) {
        return ResponseEntity.ok(trendService.getOrderTrend(range, bistroIds, branchIds));
    }

    @GetMapping("/trend/revenue")
    public ResponseEntity<List<TrendPointDto>> getRevenueTrend(
            @RequestParam List<UUID> bistroIds,
            @RequestParam(required = false) List<UUID> branchIds,
            Range range
    ) {
        return ResponseEntity.ok(trendService.getRevenueTrend(range, bistroIds, branchIds));
    }

    @GetMapping("/trend/payment-mode")
    public ResponseEntity<List<TrendPointDto>> getPaymentMode(
            @RequestParam List<UUID> bistroIds,
            @RequestParam(required = false) List<UUID> branchId,
            Range range
    ) {
        return  ResponseEntity.ok(trendService.getPaymentModeDistrubtion(bistroIds,branchId,range));
    }


}
