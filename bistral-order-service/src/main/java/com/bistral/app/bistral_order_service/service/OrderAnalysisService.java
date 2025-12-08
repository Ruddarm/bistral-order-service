package com.bistral.app.bistral_order_service.service;


import com.bistral.app.bistral_order_service.dtos.KpiDTO;
import com.bistral.app.bistral_order_service.repository.OrderAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderAnalysisService {

    private final OrderAnalysisRepository orderAnalysisRepository;


    public KpiDTO getKpis(List<UUID> bistroIds, List<UUID> branchId, String range) {
        LocalDate today = LocalDate.now();
        LocalDateTime start;
        LocalDateTime end = LocalDateTime.now();

        switch (range.toLowerCase()) {
            case "today":
                start = today.atStartOfDay();
                break;

            case "week":
                start = today.minusDays(6).atStartOfDay();
                break;

            case "month":
                start = today.withDayOfMonth(1).atStartOfDay();
                break;

            default:
                throw new IllegalArgumentException("Invalid range");
        }
        return orderAnalysisRepository.getTodayKpi(bistroIds, start,end);


    }
}

