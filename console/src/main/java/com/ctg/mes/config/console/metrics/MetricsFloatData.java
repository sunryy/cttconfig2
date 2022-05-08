package com.ctg.mes.config.console.metrics;


import com.ctg.mes.config.common.util.StorageUnit;

class MetricsFloatData extends MetricsData {
    //基于数组的结构更高效,避免了自动转类型
    private final float[] values;

    public MetricsFloatData(int size) {
        this.values = new float[size];
    }

    public MetricsFloatData(int size, float initial) {
        this.values = new float[size];
        for (int i = 0; i < size; i++) {
            this.values[i] = initial;
        }
    }

    public float getValue(int i) {
        if (i >= this.values.length) {
            throw new RuntimeException("idx is out of bound.");
        }
        return values[i];
    }

    public void setValue(int i, float v) {
        this.values[i] = v;
    }

    static class FloatMetrics {
        private final float[] values;
        int interval = 0;

        public FloatMetrics(int size) {
            this.values = new float[size];
        }

        void accumulate(MetricsFloatData data) {
            if (this.values.length != data.values.length) {
                throw new RuntimeException("value size is not same");
            }
            this.interval += data.interval;
            for (int i = 0, len = values.length; i < len; i++) {
                this.values[i] += data.values[i];
            }
        }

        float getValue(int idx) {
            if (idx >= this.values.length) {
                throw new RuntimeException("idx is out of bound.");
            }
            if (0 == interval) {
                return -1; //-1代表空值
            }
            return (float) StorageUnit.keepPoint(this.values[idx] / interval);
        }

        float getSumValue(int idx) {
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
