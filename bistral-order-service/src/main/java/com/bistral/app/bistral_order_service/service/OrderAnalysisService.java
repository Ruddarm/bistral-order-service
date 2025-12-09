package com.bistral.app.bistral_order_service.service;


import com.bistral.app.bistral_order_service.dtos.KpiDTO;
import com.bistral.app.bistral_order_service.dtos.RecentOrderDto;
import com.bistral.app.bistral_order_service.dtos.TrendPointDto;
import com.bistral.app.bistral_order_service.dtos.TrendPointDtoImpl;
import com.bistral.app.bistral_order_service.repository.OrderAnalysisRepository;
import com.bistral.app.bistral_order_service.utils.Range;
import com.bistral.app.bistral_order_service.utils.RangeResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderAnalysisService {

    private final OrderAnalysisRepository orderAnalysisRepository;
    private final RangeResolver rangeResolver;


    public KpiDTO getKpis(List<UUID> bistroIds, List<UUID> branchId, Range range) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = rangeResolver.getStart(range);
        LocalDateTime end = LocalDateTime.now();
        return orderAnalysisRepository.getTodayKpi(bistroIds, start, end);
    }

    public List<RecentOrderDto> getRecentOrders(List<UUID> bistroIds) {
        return orderAnalysisRepository.getRecentOrders(bistroIds);
    }

//    public List<TrendPointDtoImpl> getOrderTrend(List<UUID> bistroIds, Range range) {
//
//        LocalDateTime start = rangeResolver.getStart(range);
//        LocalDateTime end = LocalDateTime.now();
//        List<TrendPointDto> trendPointDtos = orderAnalysisRepository.getOrderTrend(bistroIds, "hour",
//                start, end);
//        Map<String, Double> trendPointDtoMap = new HashMap<>();
//        for (TrendPointDto t : trendPointDtos)
//            trendPointDtoMap.put(t.getLabel(), t.getValue());
//        List<TrendPointDtoImpl> trendPointDtoImplList = new ArrayList<>();
//        LocalDateTime pointer = start;
//
//        while (!pointer.isAfter(end)) {
//            String label = pointer.format(DateTimeFormatter.ofPattern("hh a")); // 12-hour format: 08 PM
//
//            double value = trendPointDtoMap.getOrDefault(
//                    pointer.format(DateTimeFormatter.ofPattern("HH")), // raw label is HH or HH24
//                    0.0
//            );
//
//            trendPointDtoImplList.add(new TrendPointDtoImpl(label, value));
//            pointer = pointer.plusHours(1);
//        }
//
//        return trendPointDtoImplList;
//
//    }
}


