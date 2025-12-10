package com.bistral.app.bistral_order_service.service.implementation;

import com.bistral.app.bistral_order_service.service.interfaces.ITrendAnalysis;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DailyGrouping implements ITrendAnalysis {
    @Override
    public String getGroupBy() {
        return "day";
    }

    @Override
    public String getSqlFormatPattern() {
        return "DD Mon";
    } // e.g. "05 Dec"

    @Override
    public List<String> expectedRawLabels(LocalDateTime start, LocalDateTime end) {
        List<String> labels = new ArrayList<>();
        LocalDateTime p = start.toLocalDate().atStartOfDay();
        DateTimeFormatter raw = DateTimeFormatter.ofPattern("dd MMM");
        while (!p.isAfter(end)) {
            labels.add(p.format(raw));
            p = p.plusDays(1);
        }
        return labels;
    }

    @Override
    public String prettyLabelFromPointer(LocalDateTime pointer) {
        return pointer.format(DateTimeFormatter.ofPattern("dd MMM"));
    }

    @Override
    public String getOrderTrendQuery(List<UUID> bistroIds) {

        String ids = bistroIds.stream()
                .map(id -> "'" + id + "'")
                .collect(Collectors.joining(","));

        return """
                    SELECT 
                        TO_CHAR(date_trunc('day', o.created_at), 'DD Mon') AS label,
                        COUNT(*) AS value
                    FROM orders o
                    WHERE o.bistro_id IN (%s)
                      AND o.created_at BETWEEN :start AND :end
                    GROUP BY date_trunc('day', o.created_at)
                    ORDER BY date_trunc('day', o.created_at)
                """.formatted(ids);
    }

    @Override
    public String getRevenueTrendQuery(List<UUID> bistroIds) {
        String ids = bistroIds.stream()
                .map(id -> "'" + id + "'")
                .collect(Collectors.joining(","));
        return """
                    SELECT 
                        TO_CHAR(date_trunc('day', o.created_at), 'DD Mon') AS label,
                        COALESCE(SUM(o.payable_amount),0)::double precision AS value
                    FROM orders o
                    WHERE o.bistro_id IN (%s)
                      AND o.created_at BETWEEN :start AND :end
                    GROUP BY date_trunc('day', o.created_at)
                    ORDER BY date_trunc('day', o.created_at)
                """.formatted(ids);
    }

}
