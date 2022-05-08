package com.ctg.mes.config.console.VO.metrics;

import java.util.ArrayList;
import java.util.List;

public class ValueHolder {
    List<String> values = new ArrayList();

    public ValueHolder() {
    }

    public ValueHolder(String... values) {
        if (values != null) {
            for (String value : values) {
                this.values.add(value);
            }
        }
    }

    public void addValue(String value) {
        this.values.add(value);
    }


    public List<String> getValues() {
        return values;
    }
}
