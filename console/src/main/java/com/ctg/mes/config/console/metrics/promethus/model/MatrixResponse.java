package com.ctg.mes.config.console.metrics.promethus.model;

import java.util.List;
import java.util.Map;

public class MatrixResponse extends PrometheusResponse {
    private MatrixData data;

    public MatrixData getData() {
        return data;
    }

    public void setData(MatrixData data) {
        this.data = data;
    }

    @Override
    public String toString(){
        return this.data.getResult().toString();
    }


    public static class MatrixData {
        private String resultType;
        private List<MatrixResult> result;

        public String getResultType() {
            return resultType;
        }

        public void setResultType(String resultType) {
            this.resultType = resultType;
        }

        public List<MatrixResult> getResult() {
            return result;
        }

        public void setResult(List<MatrixResult> result) {
            this.result = result;
        }
    }

    public static class MatrixResult {
        private Map<String, String> metric;
        private List<List<Float>> values;

        public Map<String, String> getMetric() {
            return metric;
        }

        public void setMetric(Map<String, String> metric) {
            this.metric = metric;
        }

        public List<List<Float>> getValues() {
            return values;
        }

        public void setValues(List<List<Float>> values) {
            this.values = values;
        }

        @Override
        public String toString() {
            return String.format(
                "metric: %s\nvalues: %s",
                metric.toString(),
                values == null ? "" : values.toString()
            );
        }
    }
}
