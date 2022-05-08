package com.ctg.mes.config.console.metrics;

import com.ctg.mes.config.common.util.StorageUnit;
import com.ctg.mes.config.console.VO.metrics.MetricsReqVO;
import com.ctg.mes.config.console.VO.metrics.MetricsVO;
import com.ctg.mes.config.console.VO.metrics.ValueHolder;
import com.ctg.mes.config.console.metrics.promethus.PrometheusApiClient;
import com.ctg.mes.config.console.metrics.promethus.model.MatrixResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 指标的抽象类，主要处理了各种条件的筛选和节点的分割
 */
@Slf4j
abstract public class AbstractMonitorMetrics<T extends MetricsData, M> {

    // 处理Prometheus数据
    T handle(ValueHolder valueHolder) {
        throw new UnsupportedOperationException("method must be implemented");
    }

    // 对单条监控数据进行计算（求最瞬时值或求和）
    abstract void handleData(TreeMap<Long, M> treeMap, Map.Entry<Long, M> e, T data);

    // 生成单个统计对象
    // 普通Metrics : 求和
    // InstantMetrics： 求瞬时值
    abstract M createMetrics();

    /**
     * 从统计对象中获取特定的值
     *
     * @param metrics 统计对象
     * @param idx     第几个指标，一个统计对象可能包括多个指标
     * @return null表示没数据，非空值一般大于等于0
     */
    abstract Number getValue(M metrics, int idx);

    static final String PERCENT_UNIT = "%";

    static final String BYTE_UNIT = "B";

    static final String BYTE_PER_SECOND_UNIT = "B/s";

    static final String OPERATION_PER_SECOND_UNIT = "OPS";

    static final String TPS_UNIT = "TPS";

    static final String QPS_UNIT = "QPS";

    static final String MILS_UNIT = "ms";

    static final String SEC_UNIT = "sec";

    /**
     * 暂只处理一个node的情况
     */
    public MetricsVO getMetrics(PrometheusApiClient prometheusApiClient, String timeUnit, MetricsReqVO reqVO) throws Exception {
        String instId = reqVO.getInstId();

        List<ValueHolder> valueHolders = new ArrayList<>();
        List<Long> timestamps = new ArrayList<>();
        List<Date> timeAxis = new ArrayList<>();

        Long start = reqVO.getStartTime().getTime() / 1000;
        Long end = reqVO.getEndTime().getTime() / 1000;
        String step = timeUnit;
        String timeout = "10s";
        String query = buildQuery(reqVO);
        // 多个查询，暂且认为返回时间刻度一样
        MatrixResponse prometheusResponse = prometheusApiClient.queryRange(query, start + "", end + "", step, timeout);
        List<MatrixResponse.MatrixResult> result1 = prometheusResponse.getData().getResult();
        if (!CollectionUtils.isEmpty(result1)) {
            List<List<Float>> values = result1.get(0).getValues();
            for (int k = 0; k < values.size(); k++) {
                List<Float> value = values.get(k);
                String timeStr = new BigDecimal(value.get(0)).toPlainString();
                long timestamp = Long.valueOf(timeStr) * 1000; // ms
                timestamps.add(timestamp);
                ValueHolder valueHolder = new ValueHolder(value.get(1) + "");
                valueHolders.add(valueHolder);
            }
        }


        List<T> list = new ArrayList<>();
        for (int i = 0; i < valueHolders.size(); i++) {
            long timestamp = timestamps.get(i);
            timeAxis.add(new Date(timestamp));
            T data = handle(valueHolders.get(i));
            data.setNodeId(reqVO.getNodeId());
            data.setTime(timestamp);
            list.add(data);
        }

        if (list.isEmpty()) {
            log.debug("no metrics data found . type = {}, instId = {}", reqVO.getType(), instId);
        }
        MetricsVO vo = new MetricsVO();
        vo.setTimeAxis(timeAxis);
        vo.setNodeId(reqVO.getNodeId());

        calNodeValues(vo, reqVO.getType(), reqVO.getNodeId(), list);
        setUnit(vo);
        return vo;
    }

    /**
     * 构建查询参数
     */
    private String buildQuery(MetricsReqVO reqVO) {
        StringBuilder sb = new StringBuilder();
        String metricName = reqVO.getType();
        if (!metricName.isEmpty()) {
            sb.append(metricName);
        }
        sb.append("{");
        if (!reqVO.getInstId().isEmpty()) {
            sb.append(" inst_id = ").append("\"").append(reqVO.getInstId()).append("\"").append(",");
        }
        if (!reqVO.getNodeId().isEmpty()) {
            sb.append(" node_id = ").append("\"").append(reqVO.getNodeId()).append("\"").append(",");
        }
        sb.append(" }");
        return sb.toString();
    }


    /**
     * 计算时间点
     */
    static List<Date> getTimeAxis(final long startTime, final long endTime, final long timeUnit) {
        int size = (int) ((endTime - startTime) / timeUnit);
        List<Date> timeAxis = new ArrayList<>(size);
        for (long t = startTime + timeUnit; t < endTime; t += timeUnit) {
            timeAxis.add(new Date(t));
        }
        timeAxis.add(new Date(endTime));
        return timeAxis;
    }

    /**
     * 获取指标的单位
     */
    public String getUnit() {
        return "";
    }

    /**
     * 设置指标的单位
     * 针对Byte这种单位，还需要根据数据升级一下
     */
    private void setUnit(MetricsVO vo) {
        vo.setUnit(getUnit());
        //单位是byte的时候，需要升级一下单位
        if (BYTE_UNIT.equals(vo.getUnit()) || BYTE_PER_SECOND_UNIT.equals(vo.getUnit())) {
            long max = vo.maxLongValue();
            StorageUnit unit;
            if (max >= 10 * StorageUnit.TB_BYTES) {
                unit = StorageUnit.TB;
            } else if (max >= 10 * StorageUnit.GB_BYTES) {
                unit = StorageUnit.GB;
            } else if (max >= 10 * StorageUnit.MB_BYTES) {
                unit = StorageUnit.MB;
            } else if (max >= 10 * StorageUnit.KB_BYTES) {
                unit = StorageUnit.KB;
            } else {
                return;
            }
            String newUnitStr = unit.getUnit();
            if (BYTE_PER_SECOND_UNIT.equals(vo.getUnit())) {
                newUnitStr += "/s";
            }
            vo.updateNewUnit(newUnitStr, unit);
        } else if (MILS_UNIT.equals(vo.getUnit())) {//时间类，毫秒的时候
            long max = vo.maxLongValue();
            if (max >= 10000) { //10秒后变成秒为单位
                //单位转成秒
                vo.updateSecUnit(SEC_UNIT);
            }
        }
    }

    /**
     * 获取值，少于0的数据返回空
     */
    Number getPositiveValue(long value) {
        return value < 0 ? null : value;
    }

    /**
     * 这个指标包含的值数量
     */
    int getValueSize() {
        return 1;
    }

    //新建对象
    MetricsLongData newMetricsLongData() {
        return new MetricsLongData(getValueSize());
    }

    MetricsLongData newMetricsLongData(long initialValue) {
        return new MetricsLongData(getValueSize(), initialValue);
    }

    //新建对象
    MetricsFloatData newMetricsFloatData() {
        return new MetricsFloatData(getValueSize());
    }

    MetricsFloatData newMetricsFloatData(float initialValue) {
        return new MetricsFloatData(getValueSize(), initialValue);
    }

    /**
     * 获取值，少于0的数据返回空
     */
    Number getPositiveValue(float value) {
        return value < 0 ? null : value;
    }

    //计算每个节点的值，并放到了vo里面
    void calNodeValues(MetricsVO vo, String metricType, String nodeId, List<T> dataList) throws Exception {
        TreeMap<Long, M> treeMap = calNodeValues(vo, dataList);
        for (int idx = 0, len = getValueSize(); idx < len; idx++) {
            MetricsVO.NodeValue nodeValue = new MetricsVO.NodeValue();
            nodeValue.setType(metricType);
            nodeValue.setValues(getValues(treeMap, idx));
            nodeValue.setNodeId(nodeId);
            vo.getValueAxis().add(nodeValue);
        }
    }

    /**
     * 获取指标列表,包括分桶汇总数据，再计算
     */
    TreeMap<Long, M> calNodeValues(MetricsVO vo, List<T> l) {
        //汇总数据
        TreeMap<Long, M> treeMap = createTreeMap(vo.getTimeAxis());
        for (T data : l) {
            Map.Entry<Long, M> e = treeMap.ceilingEntry(data.time);
            if (null != e) {
                handleData(treeMap, e, data);
            }
        }
        //再获取具体数据
        return treeMap;
    }

    TreeMap<Long, M> createTreeMap(List<Date> timeAxis) {
        TreeMap<Long, M> treeMap = new TreeMap<>();
        for (Date d : timeAxis) {
            treeMap.put(d.getTime(), createMetrics());
        }
        return treeMap;
    }


    /**
     * 获取指标列表
     */
    List<Number> getValues(TreeMap<Long, M> treeMap, int idx) {
        List<Number> list = new ArrayList<>(treeMap.size());
        for (M p : treeMap.values()) {
            list.add(getValue(p, idx));
        }
        return list;
    }
}
