package com.bistral.app.bistral_order_service.controllers;


import com.bistral.app.bistral_order_service.dtos.KpiDTO;
import com.bistral.app.bistral_order_service.service.OrderAnalysisService;
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

    @GetMapping("/kpi")
    public ResponseEntity<KpiDTO> getKpi(
            @RequestParam String range,
            @RequestParam List<UUID> bistroIds,
            @RequestParam List<UUID> branchIds
    ) {
        return ResponseEntity.ok(orderAnalysisService.getKpis(bistroIds, branchIds, range));
    }
}
