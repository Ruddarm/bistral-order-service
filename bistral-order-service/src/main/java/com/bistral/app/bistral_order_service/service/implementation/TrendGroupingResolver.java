package com.bistral.app.bistral_order_service.service.implementation;

import com.bistral.app.bistral_order_service.service.interfaces.ITrendGrouping;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TrendGroupingResolver {

    Map<String, ITrendGrouping> groupingMap = new HashMap<>();

    public TrendGroupingResolver(List<ITrendGrouping> strategies) {
        for (ITrendGrouping s : strategies) {
            groupingMap.put(s.getGroupBy(), s);
        }
    }
    public  ITrendGrouping getGrouping(String groupBy){
        return  groupingMap.get(groupBy);
    }

}
