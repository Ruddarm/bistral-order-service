package com.bistral.app.bistral_order_service.service.implementation;

import com.bistral.app.bistral_order_service.dtos.TrendPointDto;
import com.bistral.app.bistral_order_service.dtos.TrendPointDtoImpl;
import com.bistral.app.bistral_order_service.repository.OrderAnalysisRepository;
import com.bistral.app.bistral_order_service.repository.implementation.OrderAnalysis;
import com.bistral.app.bistral_order_service.service.interfaces.ITrendAnalysis;
import com.bistral.app.bistral_order_service.utils.Range;
import com.bistral.app.bistral_order_service.utils.RangeResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TrendService {

    private final OrderAnalysis orderAnalysis;
    private final OrderAnalysisRepository orderAnalysisRepository;
    private final RangeResolver rangeResolver;
    private final TrendGroupingResolver trendGroupingResolver;
    private final TrendFiller trendFiller;

    public List<TrendPointDto> getOrderTrend(Range range,
                                             List<UUID> bistroIds,
                                             List<UUID> branchIds) {

        LocalDateTime start = rangeResolver.getStart(range);
        LocalDateTime end = rangeResolver.getEnd(range);

        String groupBy = switch (range) {
            case TODAY -> "hour";
            case LAST_7_DAYS -> "day";
            case WEEK, THIS_MONTH -> "week";
            default -> "day";
        };

        ITrendAnalysis grouping = trendGroupingResolver.getGrouping(groupBy);
        if (grouping == null) throw new IllegalStateException("No grouping strategy for: " + groupBy);

        List<TrendPointDtoImpl> raw = orderAnalysis.getOrderTrend(grouping.getOrderTrendQuery(bistroIds), start, end);

        return trendFiller.fillAndFormat(start, end, grouping, raw);
    }

    public List<TrendPointDto> getRevenueTrend(Range range, List<UUID> bistroIds, List<UUID> branchIds) {
        LocalDateTime start = rangeResolver.getStart(range);
        LocalDateTime end = rangeResolver.getEnd(range);
        String groupBy = switch (range) {
            case TODAY -> "hour";
            case LAST_7_DAYS -> "day";
            case WEEK, THIS_MONTH -> "week";
            default -> "day";
        };
        ITrendAnalysis grouping = trendGroupingResolver.getGrouping(groupBy);
        if (grouping == null) throw new IllegalStateException("No grouping strategy for: " + groupBy);
        List<TrendPointDtoImpl> raw = orderAnalysis.getRevenueTrend(grouping.getRevenueTrendQuery(bistroIds), start, end);
        return trendFiller.fillAndFormat(start, end, grouping, raw);
    }

    public List<TrendPointDto> getPaymentModeDistrubtion(List<UUID> bistroIds, List<UUID> branchId, Range range) {
        return orderAnalysisRepository.getPaymentModeDistribution(bistroIds, rangeResolver.getStart(range), rangeResolver.getEnd(range));
    }
}
