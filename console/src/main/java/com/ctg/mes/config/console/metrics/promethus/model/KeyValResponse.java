package com.ctg.mes.config.console.metrics.promethus.model;

import java.util.List;
import java.util.Map;

public class KeyValResponse extends PrometheusResponse {
    private  List<Map<String, String>> data;

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return this.data.toString();
    }


}
