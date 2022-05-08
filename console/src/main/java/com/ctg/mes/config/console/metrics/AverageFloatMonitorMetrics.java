package com.ctg.mes.config.console.metrics;

import com.ctg.mes.config.console.VO.metrics.ValueHolder;

import java.util.Map;
import java.util.TreeMap;

/**
 * 求指标的平均值
 */
public class AverageFloatMonitorMetrics extends AbstractMonitorMetrics<MetricsFloatData, MetricsFloatData.FloatMetrics> {

    @Override
    void handleData(TreeMap<Long, MetricsFloatData.FloatMetrics> treeMap, Map.Entry<Long, MetricsFloatData.FloatMetrics> e, MetricsFloatData data) {
        e.getValue().accumulate(data);
    }

    @Override
    MetricsFloatData handle(ValueHolder valueHolder) {
        MetricsFloatData data = newMetricsFloatData();
        data.interval = 1;//用于求平均
        data.setValue(0, Float.parseFloat(valueHolder.getValues().get(0)));
        return data;
    }

    @Override
    MetricsFloatData.FloatMetrics createMetrics() {
        return new MetricsFloatData.FloatMetrics(getValueSize());
    }

    @Override
    Number getValue(MetricsFloatData.FloatMetrics p, int idx) {
        return getPositiveValue(p.getValue(idx));
    }

}
