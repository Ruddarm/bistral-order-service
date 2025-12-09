package com.bistral.app.bistral_order_service.service.interfaces;

import java.time.LocalDateTime;
import java.util.List;

public interface ITrendGrouping {

    String getGroupBy();
    String getSqlFormatPattern();
    List<String> expectedRawLabels(LocalDateTime start, LocalDateTime end);
    String prettyLabelFromPointer(LocalDateTime pointer);

}
