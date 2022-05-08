package com.ctg.mes.config.console.VO.metrics;

import com.ctg.mes.config.common.util.StorageUnit;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class MetricsVO {
    List<Date> timeAxis;
    String nodeId;
    // monitor 指标类型，可能一个图包含多个指标
    List<NodeValue> valueAxis = new ArrayList<>();
    // 单位
    String unit;

    @Getter
    @Setter
    public static class NodeValue {
        private String type;
        private String nodeId;
        private List<Number> values;
    }

    public long maxLongValue() {
        long max = 0L;
        for (NodeValue node : valueAxis) {
            for (Number number : node.getValues()) {
                if (null == number) {
                    continue;
                }
                long l = number.longValue();
                if (max < l) {
                    max = l;
                }
            }
        }
        return max;
    }

    /**
     * 只针对byte这种单位，进行升级
     */
    public void updateNewUnit(String newUnit, StorageUnit unit) {
        this.unit = newUnit;
        for (NodeValue node : valueAxis) {
            for (int i = 0, len = node.values.size(); i < len; i++) {
                Number number = node.values.get(i);
                if (null != number) {
                    long v = number.longValue();
                    if (0L != v) {
                        node.values.set(i, unit.fromBytes(v));
                    }
                }
            }
        }
    }

    /**
     * 从毫秒转到秒
     */
    public void updateSecUnit(String newUnit) {
        this.unit = newUnit;
        for (NodeValue node : valueAxis) {
            for (int i = 0, len = node.values.size(); i < len; i++) {
                Number number = node.values.get(i);
                if (null != number) {
                    long v = number.longValue();
                    if (0L != v) {
                        node.values.set(i, v / 1000);
                    }
                }
            }
        }
    }
}


