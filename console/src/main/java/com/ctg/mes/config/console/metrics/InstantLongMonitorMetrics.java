package com.ctg.mes.config.console.metrics;

import com.ctg.mes.config.console.VO.metrics.ValueHolder;

import java.util.Map;
import java.util.TreeMap;

/**
 * 这个是瞬时的，不需要求合平均
 */
public class InstantLongMonitorMetrics extends AbstractMonitorMetrics<MetricsLongData, MetricsLongData> {

    @Override
    void handleData(TreeMap<Long, MetricsLongData> treeMap, Map.Entry<Long, MetricsLongData> e, MetricsLongData data) {
        treeMap.put(e.getKey(), data);
    }

    @Override
    MetricsLongData handle(ValueHolder valueHolder) {
        MetricsLongData data = newMetricsLongData();
        data.interval = 1;//用于求平均
        data.setValue(0, Long.parseLong(valueHolder.getValues().get(0)));
        return data;
    }

    @Override
    MetricsLongData createMetrics() {
        return newMetricsLongData(-1L);
    }

    @Override
    Number getValue(MetricsLongData metrics, int idx) {
        return getPositiveValue(metrics.getValue(idx));
    }
}
