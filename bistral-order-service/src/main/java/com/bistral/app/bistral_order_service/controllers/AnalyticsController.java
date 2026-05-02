package com.bistral.app.bistral_order_service.controllers;


import com.bistral.app.bistral_order_service.dtos.*;
import com.bistral.app.bistral_order_service.service.OrderAnalysisService;
import com.bistral.app.bistral_order_service.service.implementation.TrendService;
import com.bistral.app.bistral_order_service.utils.Range;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
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
        return ResponseEntity.ok(trendService.getPaymentModeDistrubtion(bistroIds, branchId, range));
    }

    @GetMapping("/filter")
    public PageResponse<List<OrderResponse>> getFilterdOrder(
            @RequestParam(required = false) List<UUID> bistroIds,
            @RequestParam(required = false) List<UUID> branchIds,
            @RequestParam(required = false) BigDecimal minPayableAmount,
            @RequestParam(required = false) BigDecimal maxPayableAmount,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        page  = Math.max(page-1,0);
        return trendService.filterdOrder(bistroIds,
                branchIds,
                minPayableAmount,
                maxPayableAmount,
                from,
                to,page,
                size);
    }

    @PostMapping("filter/export/excel")
    public void GetExcelReport(@RequestBody ColumnRequest columnRequest , HttpServletResponse response) throws IOException {
        Workbook wb = trendService.GetExcelReport(columnRequest);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=orders.xlsx");
        wb.write(response.getOutputStream());
        wb.close();
    }

}

