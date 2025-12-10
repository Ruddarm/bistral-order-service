package com.bistral.app.bistral_order_service.service.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ITrendAnalysis {

    String getGroupBy();

    String getSqlFormatPattern();

    List<String> expectedRawLabels(LocalDateTime start, LocalDateTime end);

    String prettyLabelFromPointer(LocalDateTime pointer);

    String getOrderTrendQuery(List<UUID> bistroIds);

    String getRevenueTrendQuery(List<UUID> bistroIds);

//    String getPaymentModeTrendQuery(List<UUID> bistroIds);
}
