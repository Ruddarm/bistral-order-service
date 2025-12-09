package com.bistral.app.bistral_order_service.service.implementation;


import com.bistral.app.bistral_order_service.dtos.TrendPointDto;
import com.bistral.app.bistral_order_service.dtos.TrendPointDtoImpl;
import com.bistral.app.bistral_order_service.service.interfaces.ITrendGrouping;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TrendFiller {

    // Convert raw DB projection list -> map<label, value>
    public List<TrendPointDto> fillAndFormat(LocalDateTime start,
                                             LocalDateTime end,
                                             ITrendGrouping grouping,
                                             List<TrendPointDtoImpl> raw) {

        Map<String, Double> rawMap = raw.stream()
                .collect(Collectors.toMap(TrendPointDtoImpl::getLabel, TrendPointDtoImpl::getValue,Double::sum));

        List<TrendPointDto> out = new ArrayList<>();
        List<String> expectedRaw = grouping.expectedRawLabels(start, end);

        // iterate pointers aligned with expectedRaw to produce pretty labels
        // we need pointer values; but grouping.expectedRawLabels builds raw keys in same order
        // We'll reconstruct pointer progression to produce pretty labels consistently.
        LocalDateTime pointer = start;
        for (String rawKey : expectedRaw) {
            double value = rawMap.getOrDefault(rawKey, 0.0);

            // produce pretty label based on bucket:
            String pretty;
            switch (grouping.getGroupBy()) {
                case "hour":
                    pretty = pointer.format(DateTimeFormatter.ofPattern("hh a"));
                    pointer = pointer.plusHours(1);
                    break;
                case "day":
                    pretty = pointer.format(DateTimeFormatter.ofPattern("dd MMM"));
                    pointer = pointer.plusDays(1);
                    break;
                case "week":
                    int week = pointer.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                    int year = pointer.get(IsoFields.WEEK_BASED_YEAR);
                    pretty = "W" + week + " " + year;
                    pointer = pointer.plusWeeks(1);
                    break;
                case "month":
                default:
                    pretty = pointer.format(DateTimeFormatter.ofPattern("MMM yyyy"));
                    pointer = pointer.plusMonths(1);
                    break;
            }

            out.add(new TrendPointDtoImpl(pretty, value));
        }

        return out;
    }
}

