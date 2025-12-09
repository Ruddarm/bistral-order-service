package com.bistral.app.bistral_order_service.service.implementation;

import com.bistral.app.bistral_order_service.dtos.TrendPointDto;
import com.bistral.app.bistral_order_service.dtos.TrendPointDtoImpl;
import com.bistral.app.bistral_order_service.repository.OrderAnalysisRepository;
import com.bistral.app.bistral_order_service.service.interfaces.ITrendGrouping;
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

    private final OrderAnalysisRepository repo;
    private final RangeResolver rangeResolver;
    private final TrendGroupingResolver trendGroupingResolver;
    private final TrendFiller trendFiller;

    public List<TrendPointDto> getTrend(Range range,
                                        List<UUID> bistroIds,
                                        List<UUID> branchIds) {

        LocalDateTime start = rangeResolver.getStart(range);
        LocalDateTime end = rangeResolver.getEnd(range);

        // Map Range -> groupBy
        String groupBy = switch (range) {
            case  TODAY -> "hour";
            case LAST_7_DAYS -> "day";
            case WEEK, THIS_MONTH -> "week";
            default -> "day";
        };

        ITrendGrouping grouping = trendGroupingResolver.getGrouping(groupBy);
        if (grouping == null) throw new IllegalStateException("No grouping strategy for: " + groupBy);

        // call repo depending on metric
        List<TrendPointDtoImpl> raw = repo.getOrderTrend(bistroIds, branchIds, groupBy, start, end);

        // fill missing and pretty-format labels
        return trendFiller.fillAndFormat(start, end, grouping, raw);
    }

}
