package com.bistral.app.bistral_order_service.service.implementation;

import com.bistral.app.bistral_order_service.service.interfaces.ITrendGrouping;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class DailyGrouping implements ITrendGrouping {
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
}
