package com.bistral.app.bistral_order_service.service.implementation;

import com.bistral.app.bistral_order_service.service.interfaces.ITrendAnalysis;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TrendGroupingResolver {

    Map<String, ITrendAnalysis> groupingMap = new HashMap<>();

    public TrendGroupingResolver(List<ITrendAnalysis> strategies) {
        for (ITrendAnalysis s : strategies) {
            groupingMap.put(s.getGroupBy(), s);
        }
    }
    public ITrendAnalysis getGrouping(String groupBy){
        return  groupingMap.get(groupBy);
    }



}
