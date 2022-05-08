package com.ctg.mes.config.console.metrics;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
class MetricsData {
    String nodeId;
    long time;
    int interval;

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    void handle(ResultSet result, boolean interval) throws SQLException {
        this.nodeId = result.getString(1);
        this.time = result.getTimestamp(2).getTime();
        if (interval) {
            this.interval = result.getInt(3);
        }
    }
}
