package com.ctg.mes.config.console.metrics;

import com.ctg.mes.config.console.VO.metrics.ValueHolder;

import java.util.Map;
import java.util.TreeMap;

/**
 * 求指标的平均值
 */
public class AveragetLongMonitorMetrics extends AbstractMonitorMetrics<MetricsLongData, MetricsLongData.LongMetrics> {

    @Override
    void handleData(TreeMap<Long, MetricsLongData.LongMetrics> treeMap, Map.Entry<Long, MetricsLongData.LongMetrics> e, MetricsLongData data) {
        e.getValue().accumulate(data);
    }

    @Override
    MetricsLongData handle(ValueHolder valueHolder) {
        MetricsLongData data = newMetricsLongData();
        data.interval = 1;//用于求平均
        data.setValue(0, Long.parseLong(valueHolder.getValues().get(0)));
        return data;
    }

    @Override
    MetricsLongData.LongMetrics createMetrics() {
        return new MetricsLongData.LongMetrics(getValueSize());
    }

    @Override
    Number getValue(MetricsLongData.LongMetrics metrics, int idx) {
        return getPositiveValue(metrics.getValue(idx));
    }
}
