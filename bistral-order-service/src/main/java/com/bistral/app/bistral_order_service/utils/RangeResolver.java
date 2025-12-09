package com.bistral.app.bistral_order_service.utils;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class RangeResolver {

    public LocalDateTime getStart(Range range) {
        LocalDateTime now = LocalDateTime.now();

        switch (range) {

            case TODAY:
                return now.minusHours(12);

            case WEEK:
                return now.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();

            case LAST_7_DAYS:
                return now.minusDays(7);

            case THIS_MONTH:
                return now.withDayOfMonth(1).toLocalDate().atStartOfDay();

//            case ThreeMonth:
//                return now.minusMonths(3).withDayOfMonth(1).toLocalDate().atStartOfDay();

            case YEAR:
                return now.minusYears(1).withDayOfYear(1).toLocalDate().atStartOfDay();

            default:
                return now.minusDays(1);
        }
    }

    public LocalDateTime getEnd(Range range) {
        // end is always now for these ranges
        return LocalDateTime.now();
    }

    // Overload for custom range if needed
    public LocalDateTime getStart(LocalDateTime customStart) {
        return customStart;
    }

    public LocalDateTime getEnd(LocalDateTime customEnd) {
        return customEnd;
    }

}
