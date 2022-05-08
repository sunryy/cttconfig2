package com.ctg.mes.config.console.metrics;

class MetricsLongData extends MetricsData {
    //基于数组的结构更高效,避免了自动转类型
    private final long[] values;

    public MetricsLongData(int size) {
        this.values = new long[size];
    }

    public MetricsLongData(int size, long initial) {
        this.values = new long[size];
        for (int i = 0; i < size; i++) {
            this.values[i] = initial;
        }
    }

    public long getValue(int i) {
        if (i >= this.values.length) {
            throw new RuntimeException("idx is out of bound.");
        }
        return values[i];
    }

    public void setValue(int i, long value) {
        this.values[i] = value;
    }

    static class LongMetrics {
        final long[] values;
        int interval = 0;

        LongMetrics(int size) {
            this.values = new long[size];
        }

        void accumulate(MetricsLongData data) {
            this.interval += data.interval;
            if (this.values.length != data.values.length) {
                throw new RuntimeException("value size is not same");
            }
            for (int i = 0, len = values.length; i < len; i++) {
                this.values[i] += data.values[i];
            }
        }

        long getValue(int idx) {
            if (idx >= this.values.length) {
                throw new RuntimeException("idx is out of bound.");
            }
            if (0 == interval) {
                return -1;//-1代表空值
            }
            return this.values[idx] / interval;
        }

        long getSumValue(int idx) {
            if (idx >= this.values.length) {
                throw new RuntimeException("idx is out of bound.");
            }
            if (0 == interval) {
                return -1;//-1代表空值
            }
            return this.values[idx];
        }
    }
}
