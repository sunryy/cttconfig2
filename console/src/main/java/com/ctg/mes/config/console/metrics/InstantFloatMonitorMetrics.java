package com.ctg.mes.config.console.metrics;

import com.ctg.mes.config.console.VO.metrics.ValueHolder;

import java.util.Map;
import java.util.TreeMap;

/**
 * 这个是瞬时的，不需要求合平均
 */
public class InstantFloatMonitorMetrics extends AbstractMonitorMetrics<MetricsFloatData, MetricsFloatData> {

    @Override
    void handleData(TreeMap<Long, MetricsFloatData> treeMap, Map.Entry<Long, MetricsFloatData> e, MetricsFloatData data) {
        treeMap.put(e.getKey(), data);
    }

    @Override
    MetricsFloatData handle(ValueHolder valueHolder) {
        MetricsFloatData data = newMetricsFloatData();
        data.interval = 1;//用于求平均
        data.setValue(0, Float.parseFloat(valueHolder.getValues().get(0)));
        return data;
    }

    @Override
    MetricsFloatData createMetrics() {
        return newMetricsFloatData(-1f);
    }

    @Override
    Number getValue(MetricsFloatData metrics, int idx) {
        return getPositiveValue(metrics.getValue(idx));
    }
}
