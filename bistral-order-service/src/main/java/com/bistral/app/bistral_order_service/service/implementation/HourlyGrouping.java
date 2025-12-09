package com.bistral.app.bistral_order_service.service.implementation;

import com.bistral.app.bistral_order_service.service.interfaces.ITrendGrouping;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class HourlyGrouping implements ITrendGrouping {
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
}
