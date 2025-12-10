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
public class HourlyGrouping implements ITrendAnalysis {
    @Override
    public String getGroupBy() {
        return "hour";
    }

    @Override
    public String getSqlFormatPattern() {
        return "HH24";
    }

    @Override
    public List<String> expectedRawLabels(LocalDateTime start, LocalDateTime end) {
        List<String> labels = new ArrayList<>();
        LocalDateTime p = start;
        DateTimeFormatter raw = DateTimeFormatter.ofPattern("HH");
        while (!p.isAfter(end)) {
            labels.add(p.format(raw));
            p = p.plusHours(1);
        }
        return labels;

    }

    @Override
    public String prettyLabelFromPointer(LocalDateTime pointer) {
        return pointer.format(DateTimeFormatter.ofPattern("hh a"));
    }

    @Override
    public String getOrderTrendQuery(List<UUID> bistroIds) {

        String ids = bistroIds.stream()
                .map(id -> "'" + id + "'")
                .collect(Collectors.joining(","));

        return """
                    SELECT 
                        TO_CHAR(date_trunc('hour', o.created_at), 'HH24') AS label,
                        COUNT(*) AS value
                    FROM orders o
                    WHERE o.bistro_id IN (%s)
                      AND o.created_at BETWEEN :start AND :end
                    GROUP BY date_trunc('hour', o.created_at)
                    ORDER BY date_trunc('hour', o.created_at)
                """.formatted(ids);
    }

    @Override
    public String getRevenueTrendQuery(List<UUID> bistroIds) {
        String ids = bistroIds.stream()
                .map(id -> "'" + id + "'")
                .collect(Collectors.joining(","));
        return """
                    SELECT 
                        TO_CHAR(date_trunc('hour', o.created_at), 'HH24') AS label,
                        COALESCE(SUM(o.payable_amount),0)::double precision AS value
                    FROM orders o
                    WHERE o.bistro_id IN (%s)
                      AND o.created_at BETWEEN :start AND :end
                    GROUP BY date_trunc('hour', o.created_at)
                    ORDER BY date_trunc('hour', o.created_at)
                """.formatted(ids);
    }

//    @Override
//    public String getPaymentModeTrendQuery(List<UUID> bistroIds) {
//        String ids = bistroIds.stream()
//                .map(id -> "'" + id + "'")
//                .collect(Collectors.joining(","));
//        return """
//                        SELECT
//                            p.payment_mode AS label,
//                            COALESCE(SUM(p.amount), 0) AS value
//                        FROM payment_entity p
//                        JOIN orders o ON o.order_id = p.order_id
//                        WHERE p.paid_at BETWEEN :start AND :end
//                          AND o.bistro_id IN (:bistroIds)
//                        GROUP BY p.payment_mode
//                """;
//    }


}
